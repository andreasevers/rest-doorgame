package be.ordina.controllers;

import be.ordina.model.*;
import be.ordina.repo.GameRepository;
import be.ordina.resources.DoorResource;
import be.ordina.resources.DoorsResource;
import be.ordina.resources.GameResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import static java.util.Comparator.comparingLong;
import static java.util.stream.Collectors.toCollection;

@Controller
@RequestMapping("/games")
final class GamesController {

    private static final String STATUS_KEY = "status";

    private final GameRepository gameRepository;

    @Autowired
    GamesController(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @RequestMapping(method = RequestMethod.POST, value = "")
    ResponseEntity<Void> createGame() throws URISyntaxException {
        Game game = this.gameRepository.create();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI("http://localhost:8080/games/" + game.getId()));

        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{gameId}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_XML_VALUE })
    ResponseEntity<GameResource> showGame(@PathVariable Long gameId) throws GameDoesNotExistException {
        Game game = this.gameRepository.retrieve(gameId);
        GameResource resource = new GameResource();
        resource.setStatus(game.getStatus());

        return new ResponseEntity<GameResource>(resource, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{gameId}")
    ResponseEntity<Void> destroyGame(@PathVariable Long gameId) throws GameDoesNotExistException {
        this.gameRepository.remove(gameId);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{gameId}/doors", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_XML_VALUE })
    ResponseEntity<DoorsResource> showDoors(@PathVariable Long gameId) throws GameDoesNotExistException {
        Game game = this.gameRepository.retrieve(gameId);
        DoorsResource resource = new DoorsResource();
        for (Door door : getSortedDoors(game)) {
            DoorResource doorResource = new DoorResource(door.getId());
            doorResource.setContent(door.getContent());
            doorResource.setStatus(door.getStatus());
            resource.getDoors().add(doorResource);
        }

        return new ResponseEntity<DoorsResource>(resource, HttpStatus.OK);
    }

    private Set<Door> getSortedDoors(Game game) {
        return game.getDoors().stream().sorted(comparingLong(Door::getId)).collect(toCollection(LinkedHashSet::new));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{gameId}/doors/{doorId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_XML_VALUE })
    ResponseEntity<Void> modifyDoor(@PathVariable Long gameId, @PathVariable Long doorId, @RequestBody Map<String, String> body)
        throws MissingKeyException, GameDoesNotExistException, IllegalTransitionException, DoorDoesNotExistException {
        DoorStatus status = getStatus(body);
        Game game = this.gameRepository.retrieve(gameId);

        if (DoorStatus.SELECTED == status) {
            game.select(doorId);
        } else if (DoorStatus.OPEN == status) {
            game.open(doorId);
        } else {
            throw new IllegalTransitionException(gameId, doorId, status);
        }

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @ExceptionHandler({ GameDoesNotExistException.class, DoorDoesNotExistException.class })
    ResponseEntity<String> handleNotFounds(Exception e) {
        return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ IllegalArgumentException.class, MissingKeyException.class })
    ResponseEntity<String> handleBadRequests(Exception e) {
        return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalTransitionException.class)
    ResponseEntity<String> handleConflicts(Exception e) {
        return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
    }

    private DoorStatus getStatus(Map<String, String> body) throws MissingKeyException {
        if (body.containsKey(STATUS_KEY)) {
            String value = body.get(STATUS_KEY);

            try {
                return DoorStatus.valueOf(value.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(String.format("'%s' is an illegal value for key '%s'", value, STATUS_KEY), e);
            }
        }

        throw new MissingKeyException(STATUS_KEY);
    }
}

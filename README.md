# rest-doorgame

The REST Doorgame is an example of the [Monthy Hall problem](https://en.wikipedia.org/wiki/Monty_Hall_problem).

>Suppose you're on a game show, and you're given the choice of three doors: Behind one door is a car; behind the others, goats. You pick a door, say No. 1, and the host, who knows what's behind the doors, opens another door, say No. 3, which has a goat. He then says to you, "Do you want to pick door No. 2?" Is it to your advantage to switch your choice?

Under the standard assumptions, contestants who switch have a 2/3 chance of winning the car, while contestants who stick to their choice have only a 1/3 chance.

## Building the solution

### Create a controller
Define its RequestMapping
Inject the GamesRepository

### Create resources
Separate from domain model with simple POJOs

### Define controller methods to handle the RESTful operations
Support the following flow, calling the GamesRepository throughout:

1. Start a new game
2. Show the three doors
3. Select a door - Selecting a door will automatically open a door that holds a bad result (the hint)
4. Open the door that is not selected and not opened (the switch)
5. Show the game's status (won or lost)
6. Remove the game

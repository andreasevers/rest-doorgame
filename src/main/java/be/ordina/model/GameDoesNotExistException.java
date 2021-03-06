/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package be.ordina.model;

public final class GameDoesNotExistException extends Exception {

    private static final long serialVersionUID = 7996516744853733268L;

    private static final String MESSAGE_FORMAT = "Game '%d' does not exist";

    public GameDoesNotExistException(Long gameId) {
        super(String.format(MESSAGE_FORMAT, gameId));
    }
}

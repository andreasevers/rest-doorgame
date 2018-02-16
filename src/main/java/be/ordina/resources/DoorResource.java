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

package be.ordina.resources;

import be.ordina.model.DoorContent;
import be.ordina.model.DoorStatus;

public class DoorResource {

	private final Long id;

    private volatile DoorContent content;

    private volatile DoorStatus status;

	public DoorResource(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public DoorContent getContent() {
		return content;
	}

    public void setContent(DoorContent content) {
		this.content = content;
	}

    public DoorStatus getStatus() {
		return status;
	}

    public void setStatus(DoorStatus status) {
		this.status = status;
	}

}

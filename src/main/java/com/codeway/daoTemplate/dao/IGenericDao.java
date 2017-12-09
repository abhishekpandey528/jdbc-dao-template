package com.codeway.daoTemplate.dao;

import java.io.Serializable;
import java.util.List;

/**
*
* @author Abhishek Pandey
*          <br>
*         Copyright (c) Abhishek Pandey
*         <br><br>
*         Licensed under the Apache License, Version 2.0 (the "License");
*         you may not use this file except in compliance with the License.
*         You may obtain a copy of the License at
*         <br><br>
*         http://www.apache.org/licenses/LICENSE-2.0
*         <br><br>
*         Unless required by applicable law or agreed to in writing, software
*         distributed under the License is distributed on an "AS IS" BASIS,
*         WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*         See the License for the specific language governing permissions and
*         limitations under the License.
*/
public interface IGenericDao<Pk extends Serializable, Entity> {

	public Entity get(Pk id) throws Exception;
	public List<Entity> getAll() throws Exception;
	public Entity save(Entity entity) throws Exception;
	public Entity update(Entity entity) throws Exception;
	public void remove(Pk id) throws Exception;
}

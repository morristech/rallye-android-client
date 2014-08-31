/*
 * Copyright (c) 2014 Jakob Wenzel, Ramon Wirsch.
 *
 * This file is part of RallySoft.
 *
 * RallyeSoft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Foobar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.stadtrallye.rallyesoft.model.executors;

import java.util.Map;

import de.stadtrallye.rallyesoft.net.Request;
import de.stadtrallye.rallyesoft.util.IConverter;
import de.stadtrallye.rallyesoft.util.JSONConverter;
import de.stadtrallye.rallyesoft.util.StringedJSONArrayToMapConverter;

/**
 * Read a String in as JSON Array, generate a List<T> from it, put all entries in a Map<K, V> using the compressor to turn T in V and the indexer to generate K from T
 * @param <K> the target key type
 * @param <T> the temporary value type
 * @param <V> the target value type
 */
public class JSONArrayToMapRequestExecutor<K, T, V, ID> extends RequestExecutor<Map<K, V>, ID> {

	public JSONArrayToMapRequestExecutor(Request req, JSONConverter<T> converter, IConverter<? super T, K> indexer, IConverter<T, V> compressor, Callback<ID> callback, ID callbackId) {
		super(req, new StringedJSONArrayToMapConverter<K, T, V>(converter, indexer, compressor), callback, callbackId);
	}

}

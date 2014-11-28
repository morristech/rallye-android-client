/*
 * Copyright (c) 2014 Jakob Wenzel, Ramon Wirsch.
 *
 * This file is part of RallyeSoft.
 *
 * RallyeSoft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RallyeSoft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RallyeSoft. If not, see <http://www.gnu.org/licenses/>.
 */

package de.stadtrallye.rallyesoft.model.pictures;

import de.stadtrallye.rallyesoft.net.PictureIdResolver;

/**
* Created by Ramon on 06.10.2014.
*/
public class PictureGallery extends AbstractPictureGallery {

	private final String[] pictures;
	private final int initialPos;
	private final PictureIdResolver resolver;

	public PictureGallery(int initialPos, String[] pictures, PictureIdResolver resolver) {
		this.resolver = resolver;
		this.initialPos = initialPos;
		this.pictures = pictures;
	}

	@Override
	public int getInitialPosition() {
		return initialPos;
	}

	@Override
	public int getCount() {
		return pictures.length;
	}

	@Override
	public String getPictureUrl(int pos) {
		return resolver.resolvePictureID(pictures[pos], size);
	}

}
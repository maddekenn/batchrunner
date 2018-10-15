/*
 * Copyright 2017 Uppsala University Library
 *
 * This file is part of Cora.
 *
 *     Cora is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Cora is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Cora.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.uu.ub.cora.batchrunner.find;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.httphandler.HttpHandler;
import se.uu.ub.cora.httphandler.HttpHandlerFactory;
import se.uu.ub.cora.httphandler.HttpMultiPartUploader;

public class HttpHandlerFactorySpy implements HttpHandlerFactory {

	public String urlString;
	public HttpHandlerSpy httpHandlerSpy;
	public List<HttpHandlerSpy> httpHandlerSpies = new ArrayList<>();

	private int responseCode = 200;

	@Override
	public HttpHandler factor(String urlString) {
		this.urlString = urlString;
		try {
			URL url = new URL(urlString);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			httpHandlerSpy = HttpHandlerSpy.usingURLConnection(urlConnection);
			httpHandlerSpy.responseCode = responseCode;
			httpHandlerSpies.add(httpHandlerSpy);
			return httpHandlerSpy;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;

	}

	@Override
	public HttpMultiPartUploader factorHttpMultiPartUploader(String url) {
		// TODO Auto-generated method stub
		return null;
	}

}

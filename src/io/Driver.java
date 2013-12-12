package io;

import color.IntColor;
import environment.Environment;

public class Driver {

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		Environment environ = new Environment(false);
		IntColor[][] map = environ.getColorMap();
		ImageMaker image = new ImageMaker("test");
		image.makeImage(map);
		
		System.out.println(System.currentTimeMillis() - start);

	}

}

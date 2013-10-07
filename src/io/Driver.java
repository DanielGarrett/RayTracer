package io;

import color.IntColor;
import environment.Environment;

public class Driver {

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		Environment environ = new Environment();
		IntColor[][] map = environ.getColorMap();
		ImageMaker image = new ImageMaker();
		image.makeImage(map);
		
		System.out.println(System.currentTimeMillis() - start);

	}

}

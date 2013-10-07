package io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import color.IntColor;

public class ImageMaker {
	private BufferedImage buffer;

	public ImageMaker() {
		
	}
	
	public void makeImage(IntColor[][] colors)
	{
		buffer = new BufferedImage(colors.length,
				colors[0].length, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < colors.length; x++) {
			for (int y = 0; y < colors[x].length; y++) {
				IntColor c = colors[x][y];
				int color = c.blue;
				color += c.green * 0x100;
				color += c.red * 0x10000;
				buffer.setRGB(x, y, color);
			}
		}
		File outputfile = new File("image.png");
		try {
			ImageIO.write(buffer, "png", outputfile);
		} catch (IOException e) {
		}
	}

}

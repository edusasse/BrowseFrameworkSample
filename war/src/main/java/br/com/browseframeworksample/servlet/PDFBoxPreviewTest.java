package br.com.browseframeworksample.servlet;

import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFImageWriter;

public class PDFBoxPreviewTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			String path = "c:\\temp\\test.pdf";
			PDFBoxPreviewTest t = new PDFBoxPreviewTest();
			t.getDocument(path);
		} catch (Exception e) {

			System.out.println(e.getMessage());
		}
	}

	private PDDocument getDocument(String path) throws Exception {
		PDDocument document = null;

		document = PDDocument.load(path);

		PDFImageWriter writer = new PDFImageWriter();
		
		boolean success = writer.writeImage(document, "jpg", "", 1, 2,
				"c://temp//new//myFileName", BufferedImage.TYPE_INT_RGB, Toolkit
						.getDefaultToolkit().getScreenResolution());
		System.out.println(success);

		return document;
	}
}
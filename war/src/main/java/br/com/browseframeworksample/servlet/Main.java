package br.com.browseframeworksample.servlet;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;

public class Main {

	public static void main(String[] args) throws IOException {
		PDDocument document = PDDocument.load("c:\\temp\\test.pdf");
		List pages = document.getDocumentCatalog().getAllPages();
		Iterator iter = pages.iterator();
		while (iter.hasNext()) {
			PDPage page = (PDPage) iter.next();
			PDResources resources = page.getResources();
			Map images = resources.getImages();
			if (images != null) {
				Iterator imageIter = images.keySet().iterator();
				while (imageIter.hasNext()) {
					String key = (String) imageIter.next();
					PDXObjectImage image = (PDXObjectImage) images.get(key);
					String name = "c://temp//new//teste";
					System.out.println("Writing image:" + name);
					image.write2file(name);
					break;
				}
			}
			break;	
		}
	}

}
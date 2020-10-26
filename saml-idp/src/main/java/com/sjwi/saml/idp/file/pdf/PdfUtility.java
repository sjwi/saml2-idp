package com.sjwi.saml.idp.file.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.core.io.ClassPathResource;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.sjwi.saml.idp.model.security.KeyManager;

public class PdfUtility {

	private static final String TITLE = "Your Authorization Key is:";
	private static final String KEY_POSITION_TEXT = "position:        1       2       3       4                  5       6       7       8                 9      10      11     12              13     14     15     16      ";
	private static final String KEY_CUTOUT_POSITION_TEXT = "1     2      3     4            5      6     7     8            9    10    11   12          13   14   15   16";
	private static final String INSTRUCTIONS_IMG_FILENAME = "instructions.jpg";
	private static final String CUT_HERE_IMG_FILENAME = "cut-here.png";
	private static final String PDF_SUB_DIRECTORY = "/pdf/";
	private static final String CONFIDENTIAL_WARNING = "[CONFIDENTIAL]";
	private static final String PDF_FILE_NAME = "SecurityKey_";

	private final String user;
	private final String fileName;
	private final String path;
	/**
	 * Generated a PDF file for a user on enrollment
	 * @param key
	 * @param root
	 * @param user
	 * @throws PdfBuildException
	 */
	
	public PdfUtility (String root, String user) {
		makePdfDirectory(new File(root + PDF_SUB_DIRECTORY));

		this.fileName = PDF_FILE_NAME + user;
		this.path = root + "/pdf/"+ fileName + ".pdf";
		this.user = user;
	}

	public void buildEnrollmentPdf(KeyManager key) throws RuntimeException {
		
			Document document = new Document();
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

		try {
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));

			document.open();
			
			//page 1
			drawText(sdf.format(new Date()), 10, 820, BaseColor.BLACK, 10, FontFactory.HELVETICA, writer, Element.ALIGN_LEFT);

			drawText(user, 290, 820, BaseColor.BLACK, 13, FontFactory.HELVETICA_BOLD, writer, Element.ALIGN_CENTER);

			drawText(CONFIDENTIAL_WARNING, 500, 820, BaseColor.RED, 10, FontFactory.HELVETICA, writer, Element.ALIGN_LEFT);

			document.add(getTextParagraph(TITLE,16,FontFactory.HELVETICA));

			document.add(getTextParagraph(key.getDecryptedKeyInFourDigitTextBlocks(user).stream().collect(Collectors.joining(" ")),30,FontFactory.COURIER_BOLD));
			
			drawText(KEY_POSITION_TEXT, 91, 730, BaseColor.BLACK, 7, FontFactory.HELVETICA, writer, Element.ALIGN_LEFT);
			
			document.add(getImage(INSTRUCTIONS_IMG_FILENAME,PageSize.A4.getWidth(),PageSize.A4.getHeight(),0,235));
			
			document.add(getImage(CUT_HERE_IMG_FILENAME, 25, 25, 168, 183));
			
			drawCutOutBox(writer);
			
			drawText(key.getDecryptedKeyInFourDigitTextBlocks(user).stream().collect(Collectors.joining(" ")), 186, 120, BaseColor.BLACK, 20, FontFactory.COURIER_BOLD, writer, Element.ALIGN_LEFT);
			
			drawText(KEY_CUTOUT_POSITION_TEXT, 190, 111, BaseColor.BLACK, 6, FontFactory.HELVETICA, writer, Element.ALIGN_LEFT);
			

		} catch (Exception e) {

			throw new RuntimeException("Unable to generate PDF",e);
		}
		finally {
			document.close();
		}
	}

	private void drawText(String text, int x, int y, BaseColor color, int fontSize, String fontType, PdfWriter writer, int alignment) {
		Font font = FontFactory.getFont(fontType, fontSize, color);
		Phrase phrase = new Phrase(text,font);
		ColumnText.showTextAligned(writer.getDirectContent(), alignment, phrase, x, y, 0);
	}
	
	private Paragraph getTextParagraph(String text, int fontSize, String fontType) {
		Font font = FontFactory.getFont(fontType, fontSize, BaseColor.BLACK);
		Chunk chunk = new Chunk(text,font);
		Phrase phrase = new Phrase();
		phrase.add(chunk);
		Paragraph para = new Paragraph();
		para.add(phrase);
		para.setLeading(20,1);
		para.setAlignment(Element.ALIGN_CENTER);
		return para;
	}

	private Image getImage(String imageFileName, float scaleToFitX, float scaleToFitY, int absolutePositionX, int absolutePositionY) throws IOException, BadElementException {
		Image img = Image.getInstance(Paths.get(new ClassPathResource(imageFileName).getFile().toURI()).toAbsolutePath().toString());
		img.scaleToFit(scaleToFitX, scaleToFitY);
		img.setAbsolutePosition(absolutePositionX, absolutePositionY);
		return img;
	}

	private void drawCutOutBox(PdfWriter writer) throws IOException, DocumentException {
		PdfContentByte canvas = writer.getDirectContent();
		Rectangle rect = new Rectangle(180,50,420,190);
		rect.setBorder(Rectangle.BOX);
		rect.setBorderWidth(2);
		canvas.setLineDash(3f,3f);
		canvas.rectangle(rect);
	}
	
	/**
	 * Method to remove the PDF file from the server
	 * @param user
	 * @param root
	 * @throws PdfBuildException 
	 */
	public void delete() throws RuntimeException {
		try {
			new File(path).delete();
		}
		catch (Exception e) {
			throw new RuntimeException("Unable to remove PDF",e);
		}
	}
	
	private void makePdfDirectory(File pdfDirectory) {
		if (!pdfDirectory.exists()) {
			pdfDirectory.mkdir();
		}
	}

	public String getPath() {
		return path;
	}

	public String getFileName() {
		return fileName;
	}
}
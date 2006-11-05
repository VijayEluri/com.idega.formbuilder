package com.idega.formbuilder.presentation.renderkit;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import com.idega.formbuilder.presentation.FBPalette;
import com.idega.formbuilder.presentation.FBPaletteComponent;

public class PaletteRenderer extends Renderer {
	
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		
		writer.endElement("TABLE");
		writer.endElement("DIV");
		System.out.println("ENCODE END PALETTE RENDERER");
	}
	
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		FBPalette palette = (FBPalette) component;
		palette.initializeComponent(context);
		writer.startElement("DIV", palette);
		writer.writeAttribute("id", palette.getId(), "id");
		writer.writeAttribute("class", palette.getStyleClass(), "styleClass");
		writer.startElement("TABLE", null);
		System.out.println("ENCODE BEGIN PALETTE RENDERER");
	}
	
	public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		FBPalette palette = (FBPalette) component;
		
		int columns = palette.getColumns();
		int count = 1;
		boolean inRow = false;
		
		System.out.println("PALETTE RENDERING: " + palette.getChildren().size());
		Iterator it = palette.getChildren().iterator();
		while(it.hasNext()) {
			if((count % columns) == 1 || columns == 1) {
				System.out.println("ENCODE CHILDREN START ROW");
				writer.startElement("TR", null);
				inRow = true;
			}
			FBPaletteComponent current = (FBPaletteComponent) it.next();
			if(current != null) {
				System.out.println("ENCODE CHILDREN RENDER COMPONENT");
				writer.startElement("TD", null);
				current.encodeEnd(context);
				writer.endElement("TD");
			}
			if((count % columns) == 0 || columns == 1) {
				System.out.println("ENCODE CHILDREN END ROW");
				writer.endElement("TR");
				inRow = false;
			}
			count++;
			
		}
		if(inRow) {
			System.out.println("ENCODE CHILDREN END ROW");
			writer.endElement("TR");
		}
	}

}

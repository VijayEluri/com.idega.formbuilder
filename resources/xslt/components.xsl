<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <!-- Copyright 2006 Idega
     author: Vytautas Čivilis
    -->
    <!-- This stylesheet copies html form components from chiba generated xhtml document -->
    
      <xsl:template match="/">
				<form_components>
					<xsl:apply-templates select="html/body/form/fieldset" />
				</form_components>
			</xsl:template>

			<xsl:template match="html/body/form/fieldset">
					<xsl:copy-of select ="div[starts-with(@id, 'fbcomp_')]" />
					<xsl:copy-of select ="span[starts-with(@id, 'fbcomp_')]" />
			</xsl:template>
</xsl:stylesheet>
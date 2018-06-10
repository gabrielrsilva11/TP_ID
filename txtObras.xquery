<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="text"/>
	<xsl:template match="Catalogo">
		<xsl:text>Listagem de Livros:</xsl:text>
		<xsl:text>&#xa;</xsl:text>
		<xsl:apply-templates select="livro">
			<xsl:sort select="titulo"/>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template match="livro">
		<xsl:text>&#x9;</xsl:text>
		<xsl:value-of select="titulo"/>
		<xsl:text>&#xa;</xsl:text>
		<xsl:text>&#x9;</xsl:text>
		<xsl:value-of select="cod_autor"/>
		<xsl:text>&#xa;</xsl:text>
		<xsl:text>&#x9;</xsl:text>
		<xsl:value-of select="editora"/>
		<xsl:text>&#xa;</xsl:text>
		<xsl:text>&#x9;</xsl:text>
		<xsl:value-of select="npaginas"/>
		<xsl:text>&#xa;</xsl:text>
		<xsl:text>&#x9;</xsl:text>
		<xsl:value-of select="preco"/>
		<xsl:text>&#xa;</xsl:text>
	</xsl:template>
</xsl:stylesheet>
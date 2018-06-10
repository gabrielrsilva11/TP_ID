<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="text"/>
    <xsl:template match="Autores">
        <xsl:text>Listagem de Autores:</xsl:text>
        <xsl:text>&#xa;</xsl:text>
        <xsl:apply-templates select="autor">
            <xsl:sort select="nome"/>
        </xsl:apply-templates>
    </xsl:template>
    <xsl:template match="autor">
        <xsl:text>&#x9;</xsl:text>
        <xsl:text>Nome:</xsl:text>
        <xsl:value-of select="nome"/>
        <xsl:text>&#xa;</xsl:text>
        <xsl:text>&#x9;</xsl:text>
        <xsl:text>Data de Nascimento:</xsl:text>
        <xsl:value-of select="data_nasc"/>
        <xsl:text>&#xa;</xsl:text>
        <xsl:text>&#xa;</xsl:text>
    </xsl:template>
</xsl:stylesheet>
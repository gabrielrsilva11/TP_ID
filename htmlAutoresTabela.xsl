<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html"/>
    <xsl:template match="Autores">
        <html>
            <body>
                <h1>LISTAGEM DE Autor</h1>
                <h3>Ordenadas por nome</h3>
                <table>
                    <tr>
                        <th>Nome</th>
                        <th>Data Nascimento</th>
                        <th>Fotografia</th>
                    </tr>
                    <xsl:apply-templates select="autor">
                        <xsl:sort select="nome"/>
                    </xsl:apply-templates>
                </table>
            </body>
        </html>
    </xsl:template>
    <xsl:template match="autor">
        <tr>
            <td>
                <xsl:value-of select="nome"/>
            </td>
            <td>
                <xsl:value-of select="data_nasc"/>
            </td>
            <td>
                <img src="{fotografia}" width="150" height="200"/>
            </td>
        </tr>
    </xsl:template>
</xsl:stylesheet>

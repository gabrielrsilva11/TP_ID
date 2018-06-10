<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html"/>
    <xsl:template match="Catalogo">
        <html>
            <body>
                <h1>LISTAGEM DE OBRAS</h1>
                <h3>Ordenadas por titulo</h3>
                <table>
                    <tr>
                        <th>Titulo</th>
                        <th>Editora</th>
                        <th>Preco</th>
                        <th>Capa</th>
                    </tr>
                    <xsl:apply-templates select="livro">
                        <xsl:sort select="titulo"/>
                    </xsl:apply-templates>
                </table>
            </body>
        </html>
    </xsl:template>
    <xsl:template match="livro">
            <tr>
                <td>
                    <xsl:value-of select="titulo"/>
                </td>
                <td>
                    <xsl:value-of select="editora"/>
                </td>
                <td>
                    <xsl:value-of select="preco"/>
                </td>
                <td>
                    <img src="{imagem}" width="150" height="200"/>
                </td>
            </tr>
    </xsl:template>
</xsl:stylesheet>

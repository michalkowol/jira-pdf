<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.1" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" exclude-result-prefixes="fo">
    <xsl:template match="stories">
        <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
            <fo:layout-master-set>
                <fo:simple-page-master master-name="simpleA6" page-height="105mm" page-width="148mm" margin-top="5mm" margin-bottom="5mm" margin-left="5mm" margin-right="5mm">
                    <fo:region-body/>
                </fo:simple-page-master>
            </fo:layout-master-set>

            <fo:page-sequence master-reference="simpleA6">
                <fo:flow flow-name="xsl-region-body">
                    <xsl:apply-templates select="story"/>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>

    <xsl:template match="story">
        <fo:table width="100%" border="solid 0.5mm black">
            <fo:table-column column-width="90%"/>
            <fo:table-column column-width="10%"/>
            <fo:table-body>
                <fo:table-row>
                    <fo:table-cell border="solid 0.3mm black" height="15mm" display-align="center">
                        <fo:block-container overflow="hidden" height="15mm">
                            <fo:block font-size="14pt">
                                <fo:inline font-weight="bold">
                                    <xsl:value-of select="key"/>
                                </fo:inline>
                                <xsl:text>   </xsl:text>
                                <xsl:value-of select="summary"/>
                            </fo:block>
                        </fo:block-container>
                    </fo:table-cell>
                    <fo:table-cell border="solid 0.3mm black" display-align="center" text-align="center">
                        <fo:block font-size="14pt">
                            <fo:inline font-weight="bold">
                                <xsl:value-of select="storyPoints"/>
                            </fo:inline>
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                    <fo:table-cell wrap-option="wrap" border="solid 0.3mm black" number-columns-spanned="2" height="79mm">
                        <fo:block-container overflow="hidden" height="79mm">
                            <fo:block font-size="10pt" linefeed-treatment="preserve">
                                <xsl:value-of select="description"/>
                            </fo:block>
                        </fo:block-container>
                    </fo:table-cell>
                </fo:table-row>
            </fo:table-body>
        </fo:table>
    </xsl:template>
</xsl:stylesheet>

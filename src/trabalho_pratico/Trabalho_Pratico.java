/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho_pratico;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmValue;
import net.sf.saxon.trans.XPathException;
import org.jdom2.Attribute;
import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;

/**
  *
  * @author Gabriel Silva
 */

public class Trabalho_Pratico {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, SaxonApiException {
        new TP_Frame().setVisible(true);
    }
    
    public static void buscaAutoresFicheiro(String nomeFich) throws FileNotFoundException, IOException, SaxonApiException
    {
        Scanner input = new Scanner(new FileInputStream(nomeFich));
        String autor;
        Autores pessoa = new Autores();
        ArrayList<Livros> l = new ArrayList<Livros>();
        
        while(input.hasNextLine())
        {
            autor = input.nextLine();
  
            pessoa=Autores.getWikiFile(autor);
            l=Livros.getWookFile(autor);
            pessoa.setID(l.get(0).getCodautor());
            Autores.adicionaAutores(pessoa);
        }
        input.close();
    }
    
    //funcoes de validacao
    
    public static int validarDocumentoXSD(String xmlFile, String XSDFile) throws IOException
    {
        Document doc = XMLJDomFunctions.lerDocumentoXML(xmlFile);
        File f = new File(XSDFile);
        
        if(doc != null && f.exists()) {
            Element raiz = doc.getRootElement();
            Namespace XSI = Namespace.getNamespace("xsi","http://www.w3.org/2001/XMLSchema-instance");
            raiz.addNamespaceDeclaration(XSI);
            raiz.setAttribute(new Attribute("noNamespaceSchemaLocation", XSDFile,
            Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance")));
            
            XMLJDomFunctions.escreverDocumentoParaFicheiro(doc,xmlFile);
            
            Document docXSD = JDOMFunctions_Validar.validarXSD(xmlFile);
            if(docXSD == null)
            {
                System.out.println("invalido por xsd");
                return -1;
            }
            else
            {
                System.out.println("valido por xsd");
                return 1;
            }
        }
        return -1;
    }
    
    public static int validarDocumentoDTD(String xmlFile, String DTDFile) throws IOException
    {
        Document doc = XMLJDomFunctions.lerDocumentoXML(xmlFile);
        File f = new File(DTDFile);
        
        if(doc!= null && f.exists())
        {
            Element raiz = doc.getRootElement();
            
            DocType dtd = new DocType(raiz.getName(), DTDFile);
            doc.setDocType(dtd);
            
            XMLJDomFunctions.escreverDocumentoParaFicheiro(doc, xmlFile);
            
            Document docDTD = JDOMFunctions_Validar.validarDTD(xmlFile);
            if(docDTD == null)
            {
                System.out.println("invalido por dtd");
                return -1;
            }
            else
            {
                System.out.println("valido por dtd");
                return 1;
            }   
        }
        return -1;
    }
    
    //funcoes de pesquisa
    
    public static String procuraNome(String procura) throws SaxonApiException
    {
        String xp="//autor[contains(nome,'"+procura+"')]";
        XdmValue res = XPathFunctions.executaXpath(xp, "autores.xml");
        String s = XPathFunctions.listaResultado(res);
        if(res==null)
        {
            System.out.println("Ficheiro XML não existe");
            s = "Ficheiro XML não existe";
        }
        else if(res.size() == 0)
        {
            System.out.println("Sem resultados");
            s = "Sem resultados";
        }
        else
            System.out.println(s);
        return s;
    }
    
    public static String procuraAutorPorCod(String procura) throws SaxonApiException
    {
        String xp="//autor[@cod='"+procura+"']/nome";
        XdmValue res = XPathFunctions.executaXpath(xp, "autores.xml");
        String s = XPathFunctions.listaResultado(res);
        if(res==null)
        {
            System.out.println("Ficheiro XML não existe");
            s = "Ficheiro XML não existe";
        }
        else if(res.size() == 0)
        {
            System.out.println("Sem resultados");
            s = "Sem resultados";
        }
        else
            System.out.println(s);
        return s;
    }
    
    public static String procuraLivroISBN(String procura) throws SaxonApiException
    {
        String xp="//livro[@isbn='"+procura+"']/titulo";
        XdmValue res = XPathFunctions.executaXpath(xp, "livros.xml");
        String s = XPathFunctions.listaResultado(res);
        if(res==null)
        {
            System.out.println("Ficheiro XML não existe");
            s = "Ficheiro XML não existe";
        }
        else if(res.size() == 0)
        {
            System.out.println("Sem resultados");
            s = "Sem resultados";
        }
        else
            System.out.println(s);
        return s;
    }
    
    public static String procuraCodautor(String procura) throws SaxonApiException
    {
        String xp="//autor[contains(nome,'"+procura+"')]/@cod";
        XdmValue res = XPathFunctions.executaXpath(xp, "autores.xml");
        String s = XPathFunctions.listaResultado(res);
        if(res==null)
        {
            System.out.println("Ficheiro XML não existe");
            s = "Ficheiro XML não existe";
        }
        else if(res.size() == 0)
        {
            System.out.println("Sem resultados");
            s = "Sem resultados";
        }
        else
            System.out.println(s);
        return s;
    }
    
    public static String procuraNacionalidade(String procura) throws SaxonApiException{
        String xp="//autor[nacionalidade='"+procura+"']/nome";
        XdmValue res = XPathFunctions.executaXpath(xp, "autores.xml");
        String s = XPathFunctions.listaResultado(res);
        if(res==null)
        {
            System.out.println("Ficheiro XML não existe");
            s = "Ficheiro XML não existe";
        }
        else if(res.size() == 0)
        {
            System.out.println("Sem resultados");
            s = "Sem resultados";
        }
        else
            System.out.println(s);
        return s;
    }
    
    public static String procuraLivrosEscritor(String procura) throws SaxonApiException{
        String codautor = procuraCodautor(procura);
        codautor=codautor.replace("\n", "");
        String xp="//livro[cod_autor='"+codautor+"']/titulo";
        XdmValue res = XPathFunctions.executaXpath(xp, "livros.xml");
        String s = XPathFunctions.listaResultado(res);
        if(res==null)
        {
            System.out.println("Ficheiro XML não existe");
            s = "Ficheiro XML não existe";
        }
        else if(res.size() == 0)
        {
            System.out.println("Sem resultados");
            s = "Sem resultados";
        }
        else
            System.out.println(s);
        return s;
    }
    
    public static String procuraGenero(String procura) throws SaxonApiException
    {
        String xp="//autor[genero='"+procura+"']/nome";
        XdmValue res = XPathFunctions.executaXpath(xp, "autores.xml");
        String s = XPathFunctions.listaResultado(res);
        if(res==null)
        {
            System.out.println("Ficheiro XML não existe");
            s = "Ficheiro XML não existe";
        }
        else if(res.size() == 0)
        {
            System.out.println("Sem resultados");
            s = "Sem resultados";
        }
        else
            System.out.println(s);
        return s;
        
    }
    
    public static String somaPrecos(String pesquisa) throws SaxonApiException
    {
        String xp="sum(//livro[cod_autor='"+pesquisa+"']/preco/number())";
        XdmValue res = XPathFunctions.executaXpath(xp, "livros.xml");
        String s = XPathFunctions.listaResultado(res);
        if(res==null)
        {
            System.out.println("Ficheiro XML não existe");
            s = "Ficheiro XML não existe";
        }
        else if(res.size() == 0)
        {
            System.out.println("Sem resultados");
            s = "Sem resultados";
        }
        else
            System.out.println(s);
        return s;
    }
    
    public static String procuraPrecosMaiores(String pesquisa) throws SaxonApiException
    {
        String xp="//livro[preco>"+pesquisa+"]";
        XdmValue res = XPathFunctions.executaXpath(xp, "livros.xml");
        String s = XPathFunctions.listaResultado(res);
        if(res==null)
        {
            System.out.println("Ficheiro XML não existe");
            s = "Ficheiro XML não existe";
        }
        else if(res.size() == 0)
        {
            System.out.println("Sem resultados");
            s = "Sem resultados";
        }
        else
            System.out.println(s);
        return s;
    }
    
    public static String procuraPrecosMenores(String pesquisa) throws SaxonApiException
    {
        String xp="//livro[preco<"+pesquisa+"]";
        XdmValue res = XPathFunctions.executaXpath(xp, "livros.xml");
        String s = XPathFunctions.listaResultado(res);
        if(res==null)
        {
            System.out.println("Ficheiro XML não existe");
            s = "Ficheiro XML não existe";
        }
        else if(res.size() == 0)
        {
            System.out.println("Sem resultados");
            s = "Sem resultados";
        }
        else
            System.out.println(s);
        return s;
    }
    
    public static String procuraPaginasPreco(String pesquisa1,String pesquisa2) throws SaxonApiException
    {
        //String xp="//livro[preco<"+pesquisa1+"]"; //+" and npaginas<"+pesquisa2+"]";
        String xp="//livro[npaginas<'"+pesquisa1+"' and preco<"+pesquisa2+"]";
        XdmValue res = XPathFunctions.executaXpath(xp, "livros.xml");
        String s = XPathFunctions.listaResultado(res);
        if(res==null)
        {
            System.out.println("Ficheiro XML não existe");
            s = "Ficheiro XML não existe";
        }
        else if(res.size() == 0)
        {
            System.out.println("Sem resultados");
            s = "Sem resultados";
        }
        else
            System.out.println(s);
        return s;
    }
    
    //remover ou alterar info
    public static String removeLivroIsbn(String procura) throws IOException{
        Element raiz;
        Document doc = XMLJDomFunctions.lerDocumentoXML("livros.xml");
        String x="";
        
        if(doc == null){
            System.out.println("Ficheiro não existe");
            return "Ficheiro não existe";
        } else{
            raiz = doc.getRootElement();
        }
        
        List todosLivros = raiz.getChildren("livro");
        boolean found = false;
        for(int i=0;i<todosLivros.size();i++){
            Element livro=(Element)todosLivros.get(i);
            if(livro.getAttribute("isbn").toString().contains(procura)){
                livro.getParent().removeContent(livro);
                x="Livro removido com sucesso!";
                found = true;
            }
        }
        if(!found){
            x="Isbn " + procura + " não foi encontrado";
            
        }

        XMLJDomFunctions.escreverDocumentoParaFicheiro(doc, "livros.xml");
        return x;
    }
    
    public static String removeLivroAutor(String procura) throws IOException{
        Element raiz;
        Document doc = XMLJDomFunctions.lerDocumentoXML("livros.xml");
        
        if(doc == null)
        {
            System.out.println("Ficheiro não existe");
            return "Ficheiro não existe";
        } else
        {
            raiz = doc.getRootElement();
        }
        
        List todosLivros = raiz.getChildren("livro");
        
        boolean found = false;
        
        for(int i=0;i<todosLivros.size();i++){
            Element livro=(Element)todosLivros.get(i);
            if(procura.equals(livro.getChild("cod_autor").getText())){
                System.out.println(i);
                System.out.println("iguais");
                livro.getParent().removeContent(livro);
                found = true;
            }
        }
        
        if(!found){
            return "Livro do autor" + procura + " não foi encontrado";
            
        }
        XMLJDomFunctions.escreverDocumentoParaFicheiro(doc, "livros.xml");
        return "Livro apagado com sucesso";
    }
    
    public static String removeAutorCod(String procura) throws IOException{
        Element raiz;
        Document doc = XMLJDomFunctions.lerDocumentoXML("autores.xml");
        
        if(doc == null){
            System.out.println("Ficheiro não existe");
            return "Ficheiro não existe";
        } else{
            raiz = doc.getRootElement();
        }
        
        List todosAutores = raiz.getChildren("autor");
        boolean found = false;
        for(int i=0;i<todosAutores.size();i++){
            Element autor=(Element)todosAutores.get(i);
            if(autor.getAttribute("cod").toString().contains(procura)){
                autor.getParent().removeContent(autor);
                removeLivroAutor(procura);
                found = true;
            }
        }
        if(!found){
            return "Autor com o cod" + procura + " não foi encontrado";
        }
        
        XMLJDomFunctions.escreverDocumentoParaFicheiro(doc, "autores.xml");
        
        return "Autor removido com sucesso";
    }
    
    public static String alteraPrecoLivro(String isbn, String novoPreco) throws IOException{
        Element raiz;
        Document doc = XMLJDomFunctions.lerDocumentoXML("livros.xml");
        
        if(doc == null){
            System.out.println("Ficheiro não existe");
            return "Ficheiro não existe";
        } else{
            raiz = doc.getRootElement();
        }
        
        List todosLivros = raiz.getChildren("livro");
        boolean found = false;
        for(int i=0;i<todosLivros.size();i++){
            Element livro=(Element)todosLivros.get(i);
            if(livro.getAttribute("isbn").toString().contains(isbn))
            {
                livro.getChild("preco").setText(novoPreco);
                found = true;
            }
        }
        
        if(found == false){
            return "Livro com o ISBN: " + isbn +" nao encontrado.";
        }
        
        XMLJDomFunctions.escreverDocumentoParaFicheiro(doc, "livros.xml");
        
        return "Preco alterado";
    }

    public static String adicionaPremios(String procura, String Premio) throws IOException
    {
        Element raiz;
        Document doc = XMLJDomFunctions.lerDocumentoXML("autores.xml");
        
        if(doc == null)
        {
            System.out.println("Ficheiro não existe");
            return "Ficheiro não existe";
        } else{
            raiz = doc.getRootElement();
        }
        
        List todosAutores = raiz.getChildren("autor");
        boolean found = false;
        for(int i=0;i<todosAutores.size();i++)
        {
            Element autor = (Element)todosAutores.get(i);
            if(autor.getAttribute("cod").toString().contains(procura))
            {
                Element premios = new Element("premios").addContent(Premio);
                autor.addContent(premios);
                found = true;
            }
        }
        if(!found)
            return "Autor com o cod "+procura+" nao encontrado";
        
        XMLJDomFunctions.escreverDocumentoParaFicheiro(doc, "autores.xml");
        
        return "Premio adicionado";
    }
    
 }

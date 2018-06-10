/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho_pratico;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmValue;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
/**
 *
 * @author gabriel
 */
public class Livros {
    String ISBN, editora, preco, ano, numeropaginas, codautor, titulo, capa;
    
    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getNumeropaginas() {
        return numeropaginas;
    }

    public void setNumeropaginas(String numeropaginas) {
        this.numeropaginas = numeropaginas;
    }

    public String getCodautor() {
        return codautor;
    }

    public void setCodautor(String codautor) {
        this.codautor = codautor;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCapa() {
        return capa;
    }

    public void setCapa(String capa) {
        this.capa = capa;
    }
    
    public static ArrayList<Livros> getWookFile(String autor) throws IOException, SaxonApiException
    {
        String link = "https://www.wook.pt/pesquisa/";
        String outputFile = "./escritores/"+autor.concat("_Wook.txt").replace(" ", "");
        
        ArrayList<Livros> l= new ArrayList<Livros>();
        
        HttpRequestFunctions.httpRequest3(link, autor, outputFile);

        Scanner input = new Scanner(new FileInputStream(outputFile));
        String link_livro="", linha;
        String nome;
        String er="<a class=\\\"title-lnk\\\" href=\\\"(\\/livro\\/([a-zA-Zàáâāéíóú',.\\-_\\\\s]*)\\/[0-9]*)\\\">";
        Pattern p = Pattern.compile(er);
        Matcher m;

        String er2="<a href=\"\\/autor\\/[a-z-]*\\/[0-9]*\">([a-zA-Zàáâāéíóúêõçã',.()\\s-_]*)<\\/a>&nbsp;";
        Pattern p2 = Pattern.compile(er2);
        Matcher m2;
        
        int i=0,found=0;

        while(input.hasNextLine())
        {
             linha = input.nextLine();
             m=p.matcher(linha);
             if(m.find())
             {
                 while(found!=1)
                 {
                     linha = input.nextLine();
                     m2=p2.matcher(linha);
                     if(m2.find())
                     {
                         found=1;
                         if(m2.group(1).equals(autor))
                         {
                             link_livro=m.group(1);
                             nome = m.group(2);
                             l.add(getWookLivros(link_livro, nome));
                             adicionaLivros(l.get(i));
                             i++;
                         }
                     }
                 }
                 found=0;
             }
             if(i==5)
                 break;
        }
        input.close();
        return l;
    }

    
    public static Livros getWookLivros(String pesquisa, String nome) throws FileNotFoundException, IOException
    {   
        String link = "https://www.wook.pt".concat(pesquisa);
        Livros livro = new Livros();
        String outputFile = "./livros/"+nome.concat(".txt");
       
        HttpRequestFunctions.httpRequest3(link, "", outputFile);
        
        livro.setISBN(getWookISBN(outputFile));
        livro.setEditora(getWookEditora(outputFile));
        livro.setPreco(getWookPreco(outputFile));
        livro.setAno(getWookAno(outputFile));
        livro.setNumeropaginas(getWookPaginas(outputFile));
        livro.setCodautor(getWookCodautor(outputFile));
        livro.setTitulo(getWookTitulo(outputFile));
        livro.setCapa(getWookImagem(outputFile));
        
        return livro;
    }
    
    public static void adicionaLivros(Livros l) throws IOException, SaxonApiException
    {
        Document doc = XMLJDomFunctions.lerDocumentoXML("livros.xml");
        
        doc=addXMLLivros(l,doc);
        
        if(doc!=null)
        {
            XMLJDomFunctions.escreverDocumentoParaFicheiro(doc, "livros.xml");
        }
        
    }
    
    public static String getWookISBN(String ficheiro) throws FileNotFoundException
    {
        Scanner input = new Scanner(new FileInputStream(ficheiro));
        String linha,ISBN="";
        String er="\"isbn\": \"([0-9-]*)\"";
        Pattern p = Pattern.compile(er);
        Matcher m;
        
        while(input.hasNextLine())
        {
            linha = input.nextLine();
            m=p.matcher(linha);
            if(m.find())
            {
                ISBN=m.group(1);
                
                break;
            }
        }
        input.close();
        return ISBN;
    }
    
    public static String getWookEditora(String ficheiro) throws FileNotFoundException
    {
        
        Scanner input = new Scanner(new FileInputStream(ficheiro));
        String linha,editora="";
        
        String er="\"@type\": \"Organization\"";
        Pattern p = Pattern.compile(er);
        Matcher m;
         
        String er2="\"name\": \"([a-zA-ZàáâāéíóúêõçãÀÁÂÃÓÉÍÚÇ',.()\\s/-_&]*)\"";
        Pattern p2 = Pattern.compile(er2);
        Matcher m2;
       
        
        while(input.hasNextLine())
        {
            linha = input.nextLine();
            m=p.matcher(linha);
            if(m.find())
            {
                linha = input.nextLine();
                m2=p2.matcher(linha);
                if(m2.find())
                {
                    editora=m2.group(1);
                    
                    break;
                }
            }
        }
        input.close();
        return editora;
    }
    
    public static String getWookPreco(String ficheiro) throws FileNotFoundException
    {
        Scanner input = new Scanner(new FileInputStream(ficheiro));
        String linha,preco="";
        String er="\"price\":\"([0-9.,]*)\"";
        Pattern p = Pattern.compile(er);
        Matcher m;
        
        while(input.hasNextLine())
        {
            linha = input.nextLine();
            m=p.matcher(linha);
            if(m.find())
            {
                preco=m.group(1);
                
                break;
            }
        }
        input.close();
        return preco;
    }
    
    public static String getWookAno(String ficheiro) throws FileNotFoundException
    {
        Scanner input = new Scanner(new FileInputStream(ficheiro));
        String linha, ano="";
        String er="\"datePublished\": \"([0-9-]*)\"";
        Pattern p = Pattern.compile(er);
        Matcher m;
        
        while(input.hasNextLine())
        {
            linha=input.nextLine();
            m=p.matcher(linha);
            if(m.find())
            {
                ano=m.group(1);
                
                break;
            }       
        }
        input.close();
        return ano;
    }
    
    public static String getWookPaginas(String ficheiro) throws FileNotFoundException
    {
        Scanner input = new Scanner(new FileInputStream(ficheiro));
        String linha, paginas="";
        
        String er="\"numberOfPages\":\"([0-9]*)\"";
        Pattern p = Pattern.compile(er);
        Matcher m;
                
        while(input.hasNextLine())
        {
            linha=input.nextLine();
            m=p.matcher(linha);
            if(m.find())
            {
                paginas=m.group(1);
               
                break;
            }
                    
        }
        input.close();
        return paginas;
    }
    
    public static String getWookCodautor(String ficheiro) throws FileNotFoundException
    {
        Scanner input = new Scanner(new FileInputStream(ficheiro));
        String linha, codautor="";
        
        String er="<a id=\"productPageRightSectionTop-author-lnk\" data-id=\"([0-9]*)\"";
        Pattern p = Pattern.compile(er);
        Matcher m;
        
        while(input.hasNextLine())
        {
            linha=input.nextLine();
            m=p.matcher(linha);
            if(m.find())
            {
                codautor=m.group(1);
              
                break;
            }
        }
        input.close();
        return codautor;      
    }
    
    public static String getWookTitulo(String ficheiro) throws FileNotFoundException
    {
        Scanner input = new Scanner(new FileInputStream(ficheiro));
        String linha, titulo="";
        
        String er="id=\"productPageRightSectionTop-title-h1\">([a-zA-ZàáâāéíóúêõçãÀÁÂÃÓÉÍÚÇ',.()\\s\\-_/]*)<\\/h1>";
        Pattern p = Pattern.compile(er);
        Matcher m;
        
        while(input.hasNextLine())
        {
            linha=input.nextLine();
            m=p.matcher(linha);
            if(m.find())
            {
                titulo=m.group(1);
               
            }
        }
        input.close();
        return titulo;
    }
    
    public static String getWookImagem(String ficheiro) throws FileNotFoundException
    {
        Scanner input = new Scanner(new FileInputStream(ficheiro));
        String linha,imagem="";
        
        String er="srcset=\"";
        Pattern p = Pattern.compile(er);
        Matcher m;
        
        String er2="(https://img.wook.pt/images/[a-z-]*/[a-zA-ZàáâāéíóúêõçãÀÁÂÃÓÉÍÚÇ',.()\\s\\-_=/0-9]*/260x) 265w,";
        Pattern p2 = Pattern.compile(er2);
        Matcher m2;
       
        
        while(input.hasNextLine())
        {
            linha = input.nextLine();
            m=p.matcher(linha);
            if(m.find())
            {
                linha = input.nextLine();
                m2=p2.matcher(linha);
                if(m2.find())
                {
                    imagem=m2.group(1);
                  
                    break;
                }
            }
        }
        input.close();
        return imagem;
    }
    
    public static Document addXMLLivros(Livros li, Document doc) throws SaxonApiException
    {
        Element raiz;
        
        if(doc == null){
            raiz = new Element("Catalogo");
            doc = new Document(raiz);            
        } else {
            raiz = doc.getRootElement();
        }
        
        String xp="//livro[@isbn='"+li.getISBN()+"']";
        XdmValue res = XPathFunctions.executaXpath(xp, "livros.xml");
        String s = XPathFunctions.listaResultado(res);
        
        if(res.size()==0)
        {
            Element livro = new Element("livro");
            Attribute isbn = new Attribute("isbn",li.getISBN());
            livro.setAttribute(isbn);
            Element titulo = new Element("titulo").addContent(li.getTitulo());
            Element autor = new Element("cod_autor").addContent(li.getCodautor());
            Element editora = new Element("editora").addContent(li.getEditora());
            Element numeropaginas = new Element("npaginas").addContent(li.getNumeropaginas());
            Element preco = new Element("preco").addContent(String.valueOf(li.getPreco()));
            Element imagem = new Element("imagem").addContent(li.getCapa());
            
            livro.addContent(titulo);
            livro.addContent(autor);
            livro.addContent(editora);
            livro.addContent(numeropaginas);
            livro.addContent(preco);
            livro.addContent(imagem);

            raiz.addContent(livro);

            return doc;
        }
        else
        {
            System.out.println("Livro já existe");
        }
        return null;
    }
}

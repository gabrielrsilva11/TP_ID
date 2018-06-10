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
public class Autores {
    String ID;
    String nome, data_nasc, data_morte, fotografia, magnum, nacionalidade;
    ArrayList<String> premios;
    ArrayList<String> genero;
    ArrayList<String> ocup;

    
    public String getID() {
        return ID;
    }

    public String getNome() {
        return nome;
    }

    public String getData_nasc() {
        return data_nasc;
    }

    public String getData_morte() {
        return data_morte;
    }

    public String getFotografia() {
        return fotografia;
    }

    public String getMagnum() {
        return magnum;
    }

    public ArrayList<String> getPremios() {
        return premios;
    }

    public ArrayList<String> getGenero() {
        return genero;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public ArrayList<String> getOcup() {
        return ocup;
    }
    
    public void setID(String ID){
        this.ID = ID;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setData_nasc(String data_nasc) {
        this.data_nasc = data_nasc;
    }

    public void setData_morte(String data_morte) {
        this.data_morte = data_morte;
    }

    public void setFotografia(String fotografia) {
        this.fotografia = fotografia;
    }

    public void setMagnum(String magnum) {
        this.magnum = magnum;
    }

    public void setPremios(ArrayList<String> premios) {
        this.premios = premios;
    }

    public void setGenero(ArrayList<String> genero) {
        this.genero = genero;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public void setOcup(ArrayList<String> ocup) {
        this.ocup = ocup;
    }
    
    public static Autores getWikiFile(String autor) throws IOException, SaxonApiException
    {
        String link = "https://pt.wikipedia.org/wiki/";
        String outputFile = "./escritores/"+autor.concat("_Wiki.txt").replace(" ", "");
        Autores pessoa = new Autores();
        pessoa.setData_morte(null);
        pessoa.setNacionalidade(null);
        HttpRequestFunctions.httpRequest1(link,autor,outputFile);
        
        Scanner input = new Scanner(new FileInputStream(outputFile));
        String linha, nome="";
        
        String er="([Ee]scritor[a]?)";
        Pattern p = Pattern.compile(er);
        Matcher m;
        int found=0;
        
        while(input.hasNextLine())
        {
            linha = input.nextLine();
            m=p.matcher(linha);
            if(m.find())
            {
                System.out.println(m.group());
                
                pessoa.setNome(retiraNomeWiki(outputFile));            
                pessoa.setData_nasc(retiraDataNascimentoWiki(outputFile));            
                pessoa.setNacionalidade(retiraNacionalidadeWiki(outputFile)); 
                pessoa.setData_morte(retiraDataMorteWiki(outputFile));
        
                pessoa.setFotografia(retiraImgWiki(outputFile));
                pessoa.setOcup(retiraOcupWiki(outputFile));
                pessoa.setPremios(retiraPremWiki(outputFile));
                pessoa.setGenero(retiraGeneroWiki(outputFile));
                
                found=1;
            }
            if(found==1)
                break;
        }
        if(found==0)
        {
            pessoa.setNome("O nome que foi introduzido não é válido");
            pessoa.setData_nasc("");
            pessoa.setID("");
        }
        input.close();
        
        return pessoa;
    }
    
    public static String adicionaAutores(Autores a) throws IOException, SaxonApiException
    {
        Document doc = XMLJDomFunctions.lerDocumentoXML("autores.xml");
        
        doc=addXMLAutores(a,doc);
        
        if(doc!=null)
        {
            XMLJDomFunctions.escreverDocumentoParaFicheiro(doc, "autores.xml");
            return "Sucesso";
        }
        return "Autor já existia em ficheiro, no entanto as suas obras foram atualizadas.";
    }
       
    /*input: ficheiro - nome do ficheiro de texto onde ir buscar o nome completo
     output: nome do autor retirado da pagina da wiki
    */
    public static String retiraNomeWiki(String ficheiro) throws FileNotFoundException
    {
        Scanner input = new Scanner(new FileInputStream(ficheiro));
        int found=0;
        String linha, nome="";
        
        String er="<p><b>([a-zA-Zàáâāéíóú',.\\s]*)</b>[\\s,]";
        Pattern p = Pattern.compile(er);
        Matcher m;
        
        while(input.hasNextLine())
        {
            linha = input.nextLine();
            m=p.matcher(linha);
            if(m.find())
            {
                nome = m.group(1);
                found =1;
            }
            if(found==1)
                break;
        }
        input.close();
        return nome;
    }
    
    public static String retiraDataNascimentoWiki(String ficheiro) throws FileNotFoundException
    {
        Scanner input = new Scanner(new FileInputStream(ficheiro));
        int found=0;
        String linha;
        String data_nasc="";
        
        String er="title=\"([0-9a-zA-Zç\\s]+)\">[0-9a-zA-Zç\\s]+</a> de <a href=\"/wiki/([0-9]{4})\" title=\"[0-9]{4}\">[0-9]{4}</a>";
        Pattern p = Pattern.compile(er);
        Matcher m;
        
        while(input.hasNextLine())
        {
            linha = input.nextLine();
            m=p.matcher(linha);
            if(m.find())
            {
                data_nasc = m.group(1)+ " de " + m.group(2);
                found =1;
            }
            if(found==1)
                break;
        }
        input.close();
        return data_nasc;
    }
    
    public static String retiraNacionalidadeWiki(String ficheiro) throws FileNotFoundException
    {
        Scanner input = new Scanner(new FileInputStream(ficheiro));
        String linha, nacionalidade2="";
        
        String er1=">Nacionalidade</td>";  
        String er2="<a href=\"/wiki/[a-zA-Zàáâāéíóúê',()\\s-_%0-9]*\" title=\"[a-zA-Zàáâāéíóúê',()%\\s-_]*\">([a-zA-Zàáâāéíóúê',-_%()\\s]*)</a>";
        Pattern p1 = Pattern.compile(er1);
        Pattern p2 = Pattern.compile(er2);
        Matcher m;
        Matcher m2;
        String nacionalidade;
        
        while(input.hasNextLine())
        {
            linha = input.nextLine();
            m=p1.matcher(linha);
            if(m.find())
            {
                nacionalidade = input.nextLine();
                m2=p2.matcher(nacionalidade);
                if(m2.find()){
                    nacionalidade2 = m2.group(1);
                    return nacionalidade2;
                }
                
            }
        }
        input.close();
        return null;
    }
    
    public static String retiraDataMorteWiki(String ficheiro) throws FileNotFoundException
    {
        Scanner input = new Scanner(new FileInputStream(ficheiro));
        String linha;
        String morte;
        String data_morte="";
        String er1=">Morte</td>";  
        String er2="title=\"([0-9a-zA-Zç\\s]+)\">[0-9a-zA-Zç\\s]+</a> de <a href=\"/wiki/([0-9]{4})\" title=\"[0-9]{4}\">[0-9]{4}</a>";
        Pattern p1 = Pattern.compile(er1);
        Pattern p2 = Pattern.compile(er2);
        Matcher m;
        Matcher m2;
       
        
        while(input.hasNextLine())
        {
            linha = input.nextLine();
            m=p1.matcher(linha);
            if(m.find())
            {
                morte = input.nextLine();
                m2=p2.matcher(morte);
                if(m2.find())
                {
                    data_morte = m2.group(1)+ " de " + m2.group(2);
                    return data_morte;
                }
                
            }
        }
        input.close();
        return null;
    }
    
    public static ArrayList<String> retiraOcupWiki(String ficheiro) throws FileNotFoundException
    {
        Scanner input = new Scanner(new FileInputStream(ficheiro));
        ArrayList<String> ocup= new ArrayList<String>();
        String linha;
        int group=1;
        String er1=">Ocupação</td>";  
        String er2=">([a-zA-Zàáâāéíóúê',()\\s-_]*)[</a>]?</td>|<a href=\"/wiki/[a-zA-Z]*\"[ class=\"mw\\-redirect\"]*title=\"[a-zA-Z]*\">([a-zA-Z]*)</a>";
        Pattern p1 = Pattern.compile(er1);
        Pattern p2 = Pattern.compile(er2);
        Matcher m;
        Matcher m2;
        String ocupacao;
        
        while(input.hasNextLine())
        {
            linha = input.nextLine();
            m=p1.matcher(linha);
            if(m.find())
            {
                linha = input.nextLine();
                m2=p2.matcher(linha);
                while(m2.find())
                {
                    ocupacao = m2.group(1);
                    if(ocupacao==null)
                    {    
                        ocup.add(m2.group(2));
                    } 
                    break;
                }
                
            }
        }
        
        input.close();
        return ocup;
    }
    
    public static ArrayList<String> retiraPremWiki(String ficheiro) throws FileNotFoundException
    {
        Scanner input = new Scanner(new FileInputStream(ficheiro));
        String prem, linha,aux;
        ArrayList<String>premios=new ArrayList<String>();
        String er1=">Prémios</td>|>Prêmios</td>";
        String er2="title=\"[a-zA-Zàáâāéíóúêõ',.()\\s/-_]*\">([a-zA-Zàáâāéíóúêõ'.,()\\s/-_]*)</a> ([(0-9),\\s]*)<br />|title=\"(Prémio [a-zA-Zàáâāéíóúê\\s]*)\">|<a href=\"/wiki/[a-zA-Zàáâāéíóúê0-9%_]+\" class=\"mw-redirect\" title=\"[a-zA-Zàáâāéíóúê]*\">([a-zA-Zàáâāéíóúê\\s]*)</a> [0-9]+";
        Pattern p1=Pattern.compile(er1);
        Pattern p2=Pattern.compile(er2);
        Matcher m, m2;
        
        while(input.hasNextLine())
        {
            linha = input.nextLine();
            m=p1.matcher(linha);
            if(m.find())
            {
                while(input.hasNextLine())
                {
                    prem = input.nextLine();
                    m2=p2.matcher(prem);
                    while(m2.find())
                    {
                        aux=m2.group(1);
                        if(aux==null)
                        {
                            aux=m2.group(3);
                            if(aux==null)
                            {
                                aux=m2.group(4);
                            }
                        }
                        premios.add(aux);
                    }
                }
                break;
            }
        }
        input.close();
        
        return premios;
    }
    
    public static String retiraImgWiki(String ficheiro) throws FileNotFoundException
    {
        Scanner input = new Scanner(new FileInputStream(ficheiro));
        String linha,link="";
        String er="<meta property=\"og:image\" content=\"(https://upload.wikimedia.org/wikipedia/commons/(thumb)?/?[a-zA-Z0-9]/[a-zA-Z0-9]*/[0-9a-zA-Zàáâāéíóúêõ%',.()\\s-_]*/?[0-9a-zA-Zàáâāéíóúêõ'%,.()\\s\\-_]*)\"/>";
        Pattern p = Pattern.compile(er);
        Matcher m;
        
        while(input.hasNextLine())
        {
            linha = input.nextLine();
            m=p.matcher(linha);
            if(m.find())
            {
                link=m.group(1);
                break;
            }
        }
        input.close();
        return link;
    }
    
    public static ArrayList<String> retiraGeneroWiki(String ficheiro) throws FileNotFoundException
    {
        Scanner input = new Scanner(new FileInputStream(ficheiro));
        String linha, genero="";
        String er1=">Género literário</a>|>Gênero literário</a>";
        String er2="a href=\"/wiki/[a-zA-Z]*\" title=\"[a-zA-Z]*\">([a-zA-Z-_]*)";
        Pattern p1 = Pattern.compile(er1);
        Matcher m1;
        Pattern p2 = Pattern.compile(er2);
        Matcher m2;
        ArrayList<String> generos=new ArrayList<String>();
        
        while(input.hasNextLine())
        {
            linha = input.nextLine();
            m1=p1.matcher(linha);
            if(m1.find())
            {
                linha = input.nextLine();
                m2 = p2.matcher(linha);
                while(m2.find())
                {
                    genero = m2.group(1);
                    generos.add(genero);
                }
            }
        }
        input.close();
        
        return generos;
    }
    
    public static Document addXMLAutores(Autores a, Document doc) throws SaxonApiException
    {
        
        Element raiz;
        int i=0;
        if(doc==null)
        {
            raiz = new Element("Autores");
            doc = new Document(raiz);
            
            Element autor = new Element("autor");
            Attribute cod = new Attribute("cod", a.getID());
            autor.setAttribute(cod);
            Element nome = new Element("nome").addContent(a.getNome());
            autor.addContent(nome);
            Element data_nasc = new Element("data_nasc").addContent(a.getData_nasc());
            autor.addContent(data_nasc);
            Element fotografia = new Element("fotografia").addContent(a.getFotografia());
            autor.addContent(fotografia);

            if(a.getData_morte()!= null)
            {
                Element data_morte = new Element("data_morte").addContent(a.getData_morte());
                autor.addContent(data_morte);
            }

            if(a.getNacionalidade() != null)
            {
                Element nacionalidade = new Element("nacionalidade").addContent(a.getNacionalidade());
                autor.addContent(nacionalidade);
            }

            if(!a.getPremios().isEmpty())
            {
                for(i=0;i<a.getPremios().size();i++){
                    Element premios = new Element("premios").addContent(a.getPremios().get(i));
                    autor.addContent(premios);
                }
            }

            if(!a.getGenero().isEmpty())
            {
                for(i=0;i<a.getGenero().size();i++){
                    Element genero = new Element("genero").addContent(a.getGenero().get(i));
                    autor.addContent(genero);
                }
            }

            if(!a.getOcup().isEmpty())
            {
                for(i=0;i<a.getOcup().size();i++){
                    Element ocupacao = new Element("ocupacao").addContent(a.getOcup().get(i));
                    autor.addContent(ocupacao);
                }
            }  

            raiz.addContent(autor);
            return doc;
        }
        else
        {
            raiz = doc.getRootElement();
            
            String xp="//autor[@cod='"+a.getID()+"']";
            XdmValue res = XPathFunctions.executaXpath(xp, "autores.xml");
            String s = XPathFunctions.listaResultado(res);
            
            if(res.size()==0)
            {
                Element autor = new Element("autor");
                Attribute cod = new Attribute("cod", a.getID());
                autor.setAttribute(cod);
                Element nome = new Element("nome").addContent(a.getNome());
                autor.addContent(nome);
                Element data_nasc = new Element("data_nasc").addContent(a.getData_nasc());
                autor.addContent(data_nasc);
                Element fotografia = new Element("fotografia").addContent(a.getFotografia());
                autor.addContent(fotografia);

                if(a.getData_morte()!= null)
                {
                    Element data_morte = new Element("data_morte").addContent(a.getData_morte());
                    autor.addContent(data_morte);
                }

                if(a.getNacionalidade() != null)
                {
                    Element nacionalidade = new Element("nacionalidade").addContent(a.getNacionalidade());
                    autor.addContent(nacionalidade);
                }

                if(!a.getPremios().isEmpty())
                {
                    for(i=0;i<a.getPremios().size();i++){
                        Element premios = new Element("premios").addContent(a.getPremios().get(i));
                        autor.addContent(premios);
                    }
                }

                if(!a.getGenero().isEmpty())
                {
                    for(i=0;i<a.getGenero().size();i++){
                        Element genero = new Element("genero").addContent(a.getGenero().get(i));
                        autor.addContent(genero);
                    }
                }

                if(!a.getOcup().isEmpty())
                {
                    for(i=0;i<a.getOcup().size();i++){
                        Element ocupacao = new Element("ocupacao").addContent(a.getOcup().get(i));
                        autor.addContent(ocupacao);
                    }
                }  

                raiz.addContent(autor);
                return doc;
            }
            else{
                System.out.println("Autor já existe");
            }
        }
        
        return null;
    }
}

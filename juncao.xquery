<Catalogo>
{
    for $a in doc("autores.xml")/Autores/autor
    
    return <autor cod="{$a/@cod}">
                {$a}
                <livros>{
                    for $b in doc("livros.xml")/Catalogo/livro
                    
                    where $a/@cod = $b/cod_autor
                    
                    return $b
                    }
                </livros>
           </autor>
}
</Catalogo>
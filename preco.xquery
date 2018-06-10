<obras>{
    for $b in doc("livros.xml")/Catalogo/livro
    order by $b/preco/text()
    return $b
}</obras>

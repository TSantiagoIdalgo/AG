SELECT 
    p.name,
    p.franchise,
    p.developer,
    p.price,
    p.discount,
    pg.genre_name,
    pp.platform_name,
    pt.tags,
    COUNT(DISTINCT cki.cart_item_id) as checkout_count 
FROM product as p
LEFT JOIN cart_item as ci ON ci.product_id = p.id
LEFT JOIN checkout_items as cki ON cki.cart_item_id = ci.id
LEFT JOIN product_tags AS pt ON pt.product_id = p.id
LEFT JOIN product_genres AS pg ON pg.product_id = p.id
LEFT JOIN product_platforms AS pp ON pp.product_id = p.id
WHERE p.price BETWEEN 15 AND 100
  AND p.discount > 10
  AND p.stock > 0
GROUP BY p.id, p.name, p.franchise, p.developer, p.price, p.discount, pg.genre_name, pp.platform_name, pt.tags
ORDER BY checkout_count DESC
LIMIT 25 OFFSET 0
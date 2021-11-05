<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <title>Document</title>

    <style>
        form {
            display: flex;
            flex-flow: row;
            flex-wrap: wrap;
            border: 2px solid;
        }
        form > * {
            margin: 5px;
        }
    </style>
</head>
<body class="container-fluid">

<div class="container" id="brand">
    <h2>Brands</h2>
    <form action="/api/brand/list">
        <button type="submit">List all</button>
    </form>
    <form action="/api/brand/get">
        <label for="brand_get_id">ID</label>
        <input id="brand_get_id" type="number" name="id">
        <button type="submit">Get by ID</button>
    </form>
    <form action="/api/brand/delete">
        <label for="brand_del_id">ID</label>
        <input id="brand_del_id" type="number" name="id">
        <button type="submit">Delete by ID</button>
    </form>
    <form action="/api/brand/create" method="post">
        <label for="brand_create_name">Name</label>
        <input id="brand_create_name" name="name">
        <label for="brand_create_value_class">Value Class (business, sport etc, can be blank)</label>
        <input id="brand_create_value_class" name="valueClass">
        <button type="submit">Create Brand</button>
    </form>
    <form action="/api/brand/update" method="post">
        <label for="brand_update_id">ID</label>
        <input id="brand_update_id" type="number" name="id">
        <label for="brand_update_name">Name</label>
        <input id="brand_update_name" name="name">
        <label for="brand_update_value_class">Value Class (business, sport etc, can be blank)</label>
        <input id="brand_update_value_class" name="valueClass">
        <button type="submit">Update Brand</button>
    </form>
</div>

<div class="container" id="model">
    <h2>Models</h2>
    <form action="/api/model/list">
        <button type="submit">List all</button>
    </form>
    <form action="/api/model/get">
        <label for="model_get_id">ID</label>
        <input id="model_get_id" type="number" name="id">
        <button type="submit">Get by ID</button>
    </form>
    <form action="/api/model/delete">
        <label for="model_del_id">ID</label>
        <input id="model_del_id" type="number" name="id">
        <button type="submit">Delete by ID</button>
    </form>
    <form action="/api/model/create" method="post">
        <label for="model_create_name">Name</label>
        <input id="model_create_name" name="name">
        <label for="model_create_brand">Brand ID</label>
        <input id="model_create_brand" type="number" name="brand">
        <label for="model_create_base_price">Base Price</label>
        <input id="model_create_base_price" type="number" name="basePrice">
        <button type="submit">Create Model</button>
    </form>
    <form action="/api/model/update" method="post">
        <label for="model_update_id">ID</label>
        <input id="model_update_id" type="number" name="id">
        <label for="model_update_name">Name</label>
        <input id="model_update_name" name="name">
        <label for="model_update_brand">Brand ID</label>
        <input id="model_update_brand" type="number" name="brand">
        <label for="model_update_base_price">Base Price</label>
        <input id="model_update_base_price" type="number" name="basePrice">
        <button type="submit">Update Model</button>
    </form>
</div>

<div class="container" id="store">
    <h2>Stores</h2>
    <form action="/api/store/list">
        <button type="submit">List all</button>
    </form>
    <form action="/api/store/get">
        <label for="store_get_id">ID</label>
        <input id="store_get_id" type="number" name="id">
        <button type="submit">Get by ID</button>
    </form>
    <form action="/api/store/delete">
        <label for="store_del_id">ID</label>
        <input id="store_del_id" type="number" name="id">
        <button type="submit">Delete by ID</button>
    </form>
    <form action="/api/store/create" method="post">
        <label for="store_create_name">Name</label>
        <input id="store_create_name" name="name">
        <label for="store_create_location">Location</label>
        <input id="store_create_location" name="location">
        <button type="submit">Create Store</button>
    </form>
    <form action="/api/store/update" method="post">
        <label for="store_update_id">ID</label>
        <input id="store_update_id" type="number" name="id">
        <label for="store_update_name">Name</label>
        <input id="store_update_name" name="name">
        <label for="store_update_location">Location</label>
        <input id="store_update_location" name="location">
        <button type="submit">Update Store</button>
    </form>
</div>

<div class="container" id="stock">
    <h2>Stocks</h2>
    <form action="/api/stock/list">
        <button type="submit">List all</button>
    </form>
    <form action="/api/stock/get">
        <label for="stock_get_id">ID</label>
        <input id="stock_get_id" type="number" name="id">
        <button type="submit">Get by ID</button>
    </form>
    <form action="/api/stock/delete">
        <label for="stock_del_id">ID</label>
        <input id="stock_del_id" type="number" name="id">
        <button type="submit">Delete by ID</button>
    </form>
    <form action="/api/stock/create" method="post">
        <label for="stock_create_store">Store ID</label>
        <input id="stock_create_store" type="number" name="store">
        <label for="stock_create_car_model">Car model ID</label>
        <input id="stock_create_car_model" type="number" name="carModel">
        <label for="stock_create_price">Price</label>
        <input id="stock_create_price" type="number" name="carModel">
        <label for="stock_create_quantity">Quantity</label>
        <input id="stock_create_quantity" type="number" name="quantity">
        <button type="submit">Create Stock</button>
    </form>
    <form action="/api/stock/update" method="post">
        <label for="stock_update_id">ID</label>
        <input id="stock_update_id" type="number" name="id">
        <label for="stock_update_store">Store ID</label>
        <input id="stock_update_store" type="number" name="store">
        <label for="stock_update_car_model">Car model ID</label>
        <input id="stock_update_car_model" type="number" name="carModel">
        <label for="stock_update_price">Price</label>
        <input id="stock_update_price" type="number" name="carModel">
        <label for="stock_update_quantity">Quantity</label>
        <input id="stock_update_quantity" type="number" name="quantity">
        <button type="submit">Update Stock</button>
    </form>
</div>

</body>
</html>
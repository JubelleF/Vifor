import json, pathlib

foods = [
    {
        "food_id": 1, "food_name": "Apple", "scientific_name": "Malus domestica",
        "category": "Fruit", "image_resource": "apple",
        "benefits": [
            {"title": "Good source of dietary fiber",
             "description": "Fiber supports digestion and helps you feel full longer.",
             "category": "Fiber"}
        ],
        "nutrition": [
            {"name": "Calories", "amount": 52, "unit": "kcal", "daily_value_percent": None},
            {"name": "Fiber", "amount": 2.4, "unit": "g", "daily_value_percent": 9},
            {"name": "Vitamin C", "amount": 4.6, "unit": "mg", "daily_value_percent": 5}
        ]
    },
    {
        "food_id": 2, "food_name": "Banana", "scientific_name": "Musa acuminata",
        "category": "Fruit", "image_resource": "banana",
        "benefits": [
            {"title": "Rich in potassium",
             "description": "Potassium supports normal muscle function and healthy blood pressure.",
             "category": "Minerals"}
        ],
        "nutrition": [
            {"name": "Calories", "amount": 89, "unit": "kcal", "daily_value_percent": None},
            {"name": "Potassium", "amount": 358, "unit": "mg", "daily_value_percent": 8},
            {"name": "Vitamin C", "amount": 8.7, "unit": "mg", "daily_value_percent": 10}
        ]
    },
    {
        "food_id": 3, "food_name": "Tomato", "scientific_name": "Solanum lycopersicum",
        "category": "Vegetable", "image_resource": "tomato",
        "benefits": [
            {"title": "Source of vitamin C and lycopene",
             "description": "Vitamin C supports the immune system, and lycopene is an antioxidant.",
             "category": "Antioxidants"}
        ],
        "nutrition": [
            {"name": "Calories", "amount": 18, "unit": "kcal", "daily_value_percent": None},
            {"name": "Vitamin C", "amount": 13.7, "unit": "mg", "daily_value_percent": 15},
            {"name": "Fiber", "amount": 1.2, "unit": "g", "daily_value_percent": 4}
        ]
    },
]

out = pathlib.Path(__file__).parent.parent / "app" / "src" / "main" / "assets" / "foods.json"
out.parent.mkdir(parents=True, exist_ok=True)
out.write_text(json.dumps({"foods": foods}, indent=2, ensure_ascii=False), encoding="utf-8")
print(f"Wrote {len(foods)} foods to {out}")
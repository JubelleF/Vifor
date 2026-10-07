import json, pathlib

# (food_name, scientific_name, category)
FOODS = [
    ("Banana", "Musa acuminata", "Fruit"),
    ("Mango", "Mangifera indica", "Fruit"),
    ("Papaya", "Carica papaya", "Fruit"),
    ("Pineapple", "Ananas comosus", "Fruit"),
    ("Coconut", "Cocos nucifera", "Fruit"),
    ("Calamansi", "Citrus x microcarpa", "Fruit"),
    ("Watermelon", "Citrullus lanatus", "Fruit"),
    ("Orange", "Citrus x sinensis", "Fruit"),
    ("Apple", "Malus domestica", "Fruit"),
    ("Guava", "Psidium guajava", "Fruit"),
    ("Jackfruit", "Artocarpus heterophyllus", "Fruit"),
    ("Avocado", "Persea americana", "Fruit"),
    ("Tomato", "Solanum lycopersicum", "Vegetable"),
    ("Eggplant", "Solanum melongena", "Vegetable"),
    ("Okra", "Abelmoschus esculentus", "Vegetable"),
    ("Squash", "Cucurbita maxima", "Vegetable"),
    ("Bitter melon", "Momordica charantia", "Vegetable"),
    ("String beans", "Vigna unguiculata subsp. sesquipedalis", "Vegetable"),
    ("Cabbage", "Brassica oleracea var. capitata", "Vegetable"),
    ("Carrot", "Daucus carota", "Vegetable"),
    ("Potato", "Solanum tuberosum", "Vegetable"),
    ("Sweet potato", "Ipomoea batatas", "Vegetable"),
    ("Onion", "Allium cepa", "Vegetable"),
    ("Garlic", "Allium sativum", "Vegetable"),
    ("Cucumber", "Cucumis sativus", "Vegetable"),
]

# Fill these in from VERIFIED sources (USDA FoodData Central, DOST-FNRI Philippine
# food composition tables). Values below are approximate placeholders per 100 g.
DETAILS = {
    "Apple": {
        "benefits": [
            {"title": "Good source of dietary fiber",
             "description": "Fiber supports digestion and helps you feel full longer.",
             "category": "Fiber"}
        ],
        "nutrition": [
            {"name": "Calories", "amount": 52, "unit": "kcal", "daily_value_percent": None},
            {"name": "Fiber", "amount": 2.4, "unit": "g", "daily_value_percent": 9},
            {"name": "Vitamin C", "amount": 4.6, "unit": "mg", "daily_value_percent": 5},
        ],
    },
    "Banana": {
        "benefits": [
            {"title": "Rich in potassium",
             "description": "Potassium supports normal muscle function and healthy blood pressure.",
             "category": "Minerals"}
        ],
        "nutrition": [
            {"name": "Calories", "amount": 89, "unit": "kcal", "daily_value_percent": None},
            {"name": "Potassium", "amount": 358, "unit": "mg", "daily_value_percent": 8},
            {"name": "Vitamin C", "amount": 8.7, "unit": "mg", "daily_value_percent": 10},
        ],
    },
    "Tomato": {
        "benefits": [
            {"title": "Source of vitamin C and lycopene",
             "description": "Vitamin C supports the immune system, and lycopene is an antioxidant.",
             "category": "Antioxidants"}
        ],
        "nutrition": [
            {"name": "Calories", "amount": 18, "unit": "kcal", "daily_value_percent": None},
            {"name": "Vitamin C", "amount": 13.7, "unit": "mg", "daily_value_percent": 15},
            {"name": "Fiber", "amount": 1.2, "unit": "g", "daily_value_percent": 4},
        ],
    },
}

foods = []
for i, (name, sci, cat) in enumerate(FOODS, start=1):
    d = DETAILS.get(name, {})
    foods.append({
        "food_id": i,
        "food_name": name,
        "scientific_name": sci,
        "category": cat,
        "image_resource": name.lower().replace(" ", "_"),
        "benefits": d.get("benefits", []),
        "nutrition": d.get("nutrition", []),
    })

out = pathlib.Path(__file__).parent.parent / "app" / "src" / "main" / "assets" / "foods.json"
out.parent.mkdir(parents=True, exist_ok=True)
out.write_text(json.dumps({"foods": foods}, indent=2, ensure_ascii=False), encoding="utf-8")

missing = [f["food_name"] for f in foods if not f["benefits"] or not f["nutrition"]]
print(f"Wrote {len(foods)} foods to {out}")
print(f"{len(missing)} still need verified benefits/nutrition: {', '.join(missing)}")
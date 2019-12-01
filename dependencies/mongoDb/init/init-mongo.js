db.createUser({
        user: "wizaord",
        pwd: "Wizard38",
        roles: [
            {
                role: "readWrite",
                db: "money"
            }
        ]
})

db.moneyUser.insert({
    name: "admin",
    pass: "password"
})
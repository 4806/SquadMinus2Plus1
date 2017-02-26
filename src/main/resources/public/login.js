webix.ui({
    rows:[
        { view:"template",
            type:"header", template:"Social Wiki" },
        { view:"form",
            id:"log_form",
            width:100,
            elements:[
                { view:"text", label:"User"},
                { view:"text", type:"password", label:"Password"},
                { margin:5, cols:[
                    { view:"button", value:"Login" , type:"form" },
                    { view:"button", value:"Cancel" }
                ]}
            ]}
    ]
});
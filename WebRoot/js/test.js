table.text("name");
table.text("name", "notEmpty");
table.text("age", "isInt?5");
table.text("money","isDouble");
table.text("birthday","isDate?yyyy-MM-dd");
table.text("age", "opInt");





table.checkBox("isCheck", 1, "是", 0, "否");
table.select("orderState", "", "==请选择==", "1", "已支付", "0", "未支付");
table.appendInput();
table.check(function(obj){
	
});






















table.input("name", "notEmpty");
table.input("age", "isInt?max=100&min=1");
table.input("state", "select?1=入库&0=未入库")
table.input("birthday", "isDate?format=yyyy-MM-dd");
table.input("money", "isDouble?length=5&scale=2");
table.input("gender","radio?1=男&0=女&default=男");

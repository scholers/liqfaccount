Ext.ux.ThemeChange = Ext.extend(Ext.form.ComboBox, {
	editable : false,
	displayField : 'theme',
	valueField : 'css',
	typeAhead : true,
	mode : 'local',
	triggerAction : 'all',
	selectOnFocus : true,
	initComponent : function() {
		var themes = [
				['默认', 'ext-all.css']
		];
		this.store = new Ext.data.SimpleStore( {
			fields : ['theme', 'css'],
			data : themes
		});
		this.value = '默认';
	},
	initEvents : function() {
		this.on('collapse', function() {
			Ext.util.CSS.swapStyleSheet('theme', 'pagesExt/extjs3.3/resources/css/'+ this.getValue());
		});
	}
});
Ext.reg('themeChange', Ext.ux.ThemeChange);
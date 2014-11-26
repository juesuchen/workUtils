var IspUtils = {
	isEmpty : function (val) {
		var value = $.trim(val);
		return value == '';
	},
	isDomValueEmpty : function (domId) {
		return this.isEmpty($('#' + domId).val());
	},
	isNotEmpty : function(val) {
		return !this.isEmpty(val);
	},
	commafy : function commafy(num) {
		num = num + ""; 
		num = num.replace(/[ ]/g, "");
		if (num == "") {
			return; 
		} 
		if (isNaN(num)){
			return; 
		} 
		var index = num.indexOf("."); 
		if (index==-1) {
			var reg = /(-?\d+)(\d{3})/; 
			while (reg.test(num)) { 
				num = num.replace(reg, "$1,$2"); 
			} 
		} else {
			var intPart = num.substring(0, index); 
			var pointPart = num.substring(index + 1, num.length); 
			var reg = /(-?\d+)(\d{3})/; 
			while (reg.test(intPart)) { 
				intPart = intPart.replace(reg, "$1,$2"); 
			} 
			num = intPart +"."+ pointPart; 
		} 
		return num; 
	},
	delcommafy :function (num){
	   num = num.replace(/[ ]/g, "");
	   num=num.replace(/,/gi,'');
	   return num;
	},
	initButtons : function(timeout) {
		timeout = timeout || 3000;
		$('button:enabled,input[type="button"]:enabled').click(function(){
			if (this.className == 'close') {
				return true;
			}
			$(this).attr('disabled',true);
			setTimeout("$('button,input[type=\"button\"]').attr('disabled',false);",timeout);
		}).css('cursor', 'pointer');
	}
};
/**
 * add by juesu.chen
 */

(function ($) {
	
	var firstLocale = {en:'First',zh_CN:'首页'};
	var prevLocale = {en:'Previous',zh_CN:'上一页'};
	var nextLocale = {en:'Next',zh_CN:'下一页'};
	var lastLocale = {en:'Last',zh_CN:'末页'};
	var defaultLocale = 'zh_CN';
	
	$.doMyPage = function(el,action,renderFun,option) {
		
		var pageChangeFun = function (num, type) {
			var page = {pageSize:options.pageSize,currentPage:num};
			$.getJSON(action,page,function(data){
				$(el).jqPaginator('option', {
		    	    currentPage: data.currentPage,
		    	    totalCounts: data.totalCount
		    	});
				renderFun(data);
			});
			
		};
		
		option = option || {};
		option.locale = option.locale || defaultLocale ;
		
		var defaultOption = {
			pageSize: 15,
			totalCounts: 1,
		    first: '<li class="first"><a href="javascript:void(0);">' + firstLocale[option.locale] + '<\/a><\/li>',
		    prev: '<li class="prev"><a href="javascript:void(0);"><i class="arrow arrow2"><\/i>' + prevLocale[option.locale] + '<\/a><\/li>',
		    next: '<li class="next"><a href="javascript:void(0);">' + nextLocale[option.locale] + '<i class="arrow arrow3"><\/i><\/a><\/li>',
		    last: '<li class="last"><a href="javascript:void(0);">' + lastLocale[option.locale] + '<\/a><\/li>',
		    page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
		    onPageChange: pageChangeFun
		};
		
		var options = $.extend({}, defaultOption, option);
		$.jqPaginator(el, options);
	};
	
})(jQuery);
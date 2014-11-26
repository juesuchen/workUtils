<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@include file="/head.jsp"%>
<link rel="stylesheet" type="text/css" href="js/third/jqPaginator/css/jqPaginator.css" />
<script type="text/javascript" src="js/third/jqPaginator/jqPaginator.js"></script>
<script type="text/javascript" src="js/paginte.js"></script>
<title><%=ms.getMessage("TITLE_TRANS") %></title>
<script type="text/javascript">
	var dialogFn = null;
	var detailIds = ['fromAcc','amount','toAcc','toName','toMobile','status','createDate'];
	function showDetail(id) {
		$.getJSON('Trans',{act:'getTransById',id:id},function(data){
			$('#fromAcc').text(data.trans.fromAcc);
			$('#amount').text(IspUtils.commafy(data.trans.amount));
			$('#toAcc').text(data.trans.toAcc);
			$('#toName').text(data.trans.toName);
			$('#toMobile').text(data.trans.toMobile);
			var status = '<%=ms.getMessage("LBL_TRANSFER_STATUS_FAILURE") %>';
			if (data.trans.status == '1') {
				status = '<%=ms.getMessage("LBL_TRANSFER_STATUS_SUCCESS") %>';
			}
			$('#status').text(status);
			$('#createDate').text(data.createDate);
			var recDiv = $('#showDiv');
			dialogFn = jw.dialog({id:'#showDiv',title:'<%=ms.getMessage("LBL_TRANSFER_DETAIL") %>',width:recDiv.width(),height:recDiv.height(),max:false});
		});
	}
	$(document).ready(function(){
		var renderPageList = function(page) {
			var pageList = page.result;
			var htmlArray = new Array();
			$.each(pageList,function(i,obj){
				var trans = obj.trans;
				htmlArray.push('<div class="trade_c_list">');
				htmlArray.push('<span class="w30">' + obj.createDate + '</span>');
				if (obj.transIn) {
					htmlArray.push('<span class="w30" style="color: green;"><%=ms.getMessage("LBL_TRANSFER_TYPE_IN") %></span>');
				} else {
					htmlArray.push('<span class="w30"><%=ms.getMessage("LBL_TRANSFER_TYPE_OUT") %></span>');
				}
				htmlArray.push('<span class="w20 mon">' + IspUtils.commafy(trans.amount) + '</span>');
				
				if (trans.status == '1') {
					htmlArray.push('<span class="w10"><%=ms.getMessage("LBL_TRANSFER_STATUS_SUCCESS") %></span>');
				} else {
					htmlArray.push('<span class="w10"><%=ms.getMessage("LBL_TRANSFER_STATUS_FAILURE") %></span>');
				}
				htmlArray.push('<span class="w10"><a href="javascript:showDetail(' + trans.id + ')"><%=ms.getMessage("BTN_TRANSFER_DETAIL") %></a></span>');
				htmlArray.push('</div>');
			});
			$('#pageList').html(htmlArray.join(''));
		}
		var option = {
			locale: '<%=ms.getMessage("LOCALE") %>'
		};
		$.doMyPage('#pagination','Trans?act=queryByPage&random='+ new Date().getTime(),renderPageList,option);
	});
</script>
</head>
<body>
<%@include file="/headBody.jsp"%>
<table width="1260" border="0" align="center" cellpadding="0" cellspacing="0" class="table_content">
  <tr>
    <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><div class="head_pic">
    <img src="images/pic.png" width="86" height="86" />
    </div>
    <div class="user_info"><div>
    <b>${sessionScope.loginInfo.userName }</b><%=ms.getMessage("LBL_HALLO") %><br />
<%=ms.getMessage("LBL_WELCOME") %></div>
      <div class="user_info_c"><%=ms.getMessage("LBL_ACCOUNT_NAME") %>${sessionScope.loginInfo.userId }   &nbsp;&nbsp; &nbsp;&nbsp;| &nbsp;&nbsp; &nbsp;&nbsp; <%=ms.getMessage("LBL_SECURITY") %> &nbsp;&nbsp; &nbsp;&nbsp;| &nbsp;&nbsp; &nbsp;&nbsp;<%=ms.getMessage("LBL_LAST_LOGIN_TIME") %>${sessionScope.loginInfo.userInfoVO.lastLoginTime }</div>
    
    </div>
    </td>
    <td width="30">&nbsp;</td>
    <td width="450" >
</td>
  </tr>
</table>
</td>
  </tr>
  <tr>
    <td valign="top">
    
    <table width="100%" border="0" cellspacing="0" cellpadding="5">
  <tr>
    <td width="18%" valign="top"><div class="payment_leftmenu"><ul><li><a href="Trans?act=toTrans"><%=ms.getMessage("TITLE_TRANS") %></a></li><li><a href="#"><%=ms.getMessage("LBL_TRANSFER_LINKED_ACCOUNTS") %></a></li>
    <li><a href="#"><%=ms.getMessage("LBL_TRANSFER_WIRE") %></a></li><li><a href="#"><%=ms.getMessage("LBL_TRANSFER_EXTERNAL") %></a></li></ul></div></td>
    <td><div class="trade_title"><%=ms.getMessage("TITLE_TRANSFER") %></div>
    <div class="trade_c_t"><span class="w30"><%=ms.getMessage("TITLE_TRANSFER_TIME") %></span><span class="w30"><%=ms.getMessage("TITLE_TRANSFER_TYPE") %></span><span class="w20"><%=ms.getMessage("TITLE_TRANSFER_AMOUNT") %></span><span class="w10"><%=ms.getMessage("TITLE_TRANSFER_STATUS") %></span><span class="w10"><%=ms.getMessage("LBL_TRANSFER_DETAIL") %></span></div>
   	<div id="pageList">
   	</div>
    </td>
  </tr>
  <tr>
  	<td colspan="2" align="right">
  		<ul class="pagination" id="pagination"></ul>
  	</td>
  </tr>
</table>

    
    
    </td>
  </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="mt5">
  <tr>
    <td background="images/demo_login_15.jpg"><div align="center"><img src="images/demo_login_16.jpg" width="1260" height="493" /></div></td>
  </tr>
</table>
<div id="showDiv" style="height: 330px;width: 400px;overflow: auto;display: none;text-align: center;">
	<table class="gridtable" width="100%" align="right" style="border: 0px;">
		<tbody>
				<tr><td class="labelTd"><%=ms.getMessage("LBL_TRANSFER_FROM") %></td><td class="textTd" id="fromAcc"></td></tr>
				<tr><td class="labelTd"><%=ms.getMessage("LBL_TRANSFER_TO") %></td><td class="textTd" id="toAcc"></td></tr>
				<tr><td class="labelTd"><%=ms.getMessage("LBL_TRANSFER_AMOUNT") %></td><td class="textTd" id="amount"></td></tr>
				<tr><td class="labelTd"><%=ms.getMessage("LBL_TRANSFER_TO_NAME") %></td><td class="textTd" id="toName"></td></tr>
				<tr><td class="labelTd"><%=ms.getMessage("LBL_TRANSFER_TO_PHONE") %></td><td class="textTd" id="toMobile"></td></tr>
				<tr><td class="labelTd"><%=ms.getMessage("LBL_TRANSFER_STATUS") %></td><td class="textTd" id="status"></td></tr>
				<tr><td class="labelTd"><%=ms.getMessage("LBL_TRANSFER_TIME") %></td><td class="textTd" id="createDate"></td></tr>
		</tbody>
		<tfoot>
			<tr><td colspan="5" align="center" style="border: 0px;line-height: normal;"><button id="backBtn" onclick="dialogFn.close();"><%=ms.getMessage("BTN_BACK") %></button></td></tr>
		</tfoot>
	</table>
</div>
</body>
</html>

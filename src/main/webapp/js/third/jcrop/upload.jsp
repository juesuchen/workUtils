<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="com.isprint.yessafe.server.YESsafeTokenServer.system.MessageSource"%>
<%	
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
	MessageSource ms = new MessageSource(request);
	String uid = request.getParameter("uid");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>上传图片</title>
<link rel="stylesheet" href="css/style.css" type="text/css" />
<link rel="stylesheet" href="js/third/jcrop/css/jquery.Jcrop.css" type="text/css" />
<link rel="stylesheet" type="text/css" href="js/third/jw/jw.css" />
<script type="text/javascript">
	var ok_text = '<%=ms.getMessage("BTN_OK")%>';
	var alert_title = '<%=ms.getMessage("TITLE_PROMPT")%>';
</script>
<script type="text/javascript" src="js/third/jquery-1.7.2.js"></script>
<script type="text/javascript" src="js/third/upload/jquery.upload.v2.js"></script>
<script type="text/javascript" src="js/third/jcrop/js/jquery.Jcrop.js"></script>
<script type="text/javascript" src="js/third/jw/jw.js"></script>
<script type="text/javascript">

$(document).ready(function(){
	
	var saveDir = '/photos/';
	var saveFileName = '<%=uid %>';
	
	var jcrop_api = null,boundx,boundy,sc = null,
	$preview = $('#preview-pane'),
	$pcnt = $('#preview-pane .preview-container'),
	$pimg = $('#preview-pane .preview-container img'),
	$fileName = null,$oldFileName = null,
	xsize = $pcnt.width(),
	ysize = $pcnt.height();
	
	function doJcrop() {
		$('#target').Jcrop({
		    onChange: updatePreview,
		    onSelect: updatePreview,
		    allowSelect : false,
		    setSelect: [100, 100, 380, 330],
		    boxWidth : 500,
		    minSize: [180, 230],
		    //maxSize: [270, 345],
		    aspectRatio: xsize / ysize
		},function(){
		    // Use the API to get the real image size
		    var bounds = this.getBounds();
		    boundx = bounds[0];
		    boundy = bounds[1];
		    // Store the API in the jcrop_api variable
		    jcrop_api = this;
		    // Move the preview into the jcrop container for css positioning
		    $preview.appendTo(jcrop_api.ui.holder);
		});
	};
	
	function updatePreview(c){
	  if (parseInt(c.w) > 0){
		sc = c;
		var rx = xsize / c.w;
		var ry = ysize / c.h;
		$pimg.css({
		  width: Math.round(rx * boundx) + 'px',
		  height: Math.round(ry * boundy) + 'px',
		  marginLeft: '-' + Math.round(rx * c.x) + 'px',
		  marginTop: '-' + Math.round(ry * c.y) + 'px'
		});
	  }
	};
    $("#upload").upload({
        action: "Upload",
        fileName: "uploadFile",
        params: {fileName:'',oldFileName:''},
        accept: ".jpg,.png",
        complete: function () {
        	if ($fileName != null) {
        		$('#targetPreview').attr('src','<%=basePath%>' + $fileName.val());
            	$('#target').attr('src','<%=basePath%>' + $fileName.val());
            	if (jcrop_api != null) {
            		jcrop_api.destroy();
            	}
            	doJcrop();
         	   	$('#uploadContent').show();
        	}
        },
        submit: function () {
        	if ($fileName == null) {
        		$fileName = $('input[name="fileName"]');
        		$oldFileName = $('input[name="oldFileName"]');
        	}
        	$oldFileName.val($fileName.val());
        	var uploadFile = $('input[name="uploadFile"]').val();
        	var tempFileName = saveDir + '_tmp/' + new Date().getTime() + uploadFile.substring(uploadFile.lastIndexOf('.'))
        	$fileName.val(tempFileName);
        	$("#msgDiv").html('');
        }
    });
    
    $("#done").click(function(){
    	if (checkCoords()) {
    		$('#src').val($fileName.val());
    		$('#dest').val(saveDir + saveFileName);
    		
    		$('#x').val(sc.x);
    		$('#y').val(sc.y);
    		$('#w').val(sc.w);
    		$('#h').val(sc.h);
    		$.getJSON('Upload',$('#form1').serialize(),function(result){
    			$("#msgDiv").html(result.msg);
    			if(result.success == true) {
    				$('.close').click();
    			}
    		});
    	}
    });
    function checkCoords(){
   	  	if (sc != null) return true;
   	 	$("#msgDiv").html('请先上传图片');
   	  	return false;
   	};
 });
 
 </script>
 <style type="text/css">

.jcrop-holder #preview-pane {
  display: block;
  position: absolute;
  z-index: 30;
  top: 10px;
  right: -280px;
  padding: 6px;
  border: 1px rgba(0,0,0,.4) solid;
  background-color: white;

  -webkit-border-radius: 6px;
  -moz-border-radius: 6px;
  border-radius: 6px;

  -webkit-box-shadow: 1px 1px 5px 2px rgba(0, 0, 0, 0.2);
  -moz-box-shadow: 1px 1px 5px 2px rgba(0, 0, 0, 0.2);
  box-shadow: 1px 1px 5px 2px rgba(0, 0, 0, 0.2);
}

#preview-pane .preview-container {
  width: 180px;
  height: 230px;
  overflow: hidden;
}

</style>
</head>
<body>
	<div style="margin: 10px;text-align:left;">
		<input type="button" value="上传图片" id="upload"/>&nbsp;&nbsp;&nbsp;&nbsp; <input type="button" value="返回" class="close" />
		&nbsp;&nbsp;&nbsp;&nbsp;<span class="msg" id="msgDiv"></span>
	</div>
	<div id='uploadContent' style="display: none;text-align: center;margin: 10px;">
		<img src="" id="target" alt="[Jcrop Example]"/>
	  	<div id="preview-pane">
		    <div class="preview-container">
		      <img src="" id="targetPreview" class="jcrop-preview" alt="Preview" />
		    </div>
		    <div style="text-align: center;padding-top:8px;"><input type="button" value="确定" id="done"/></div>
	  	</div>
	  	
  	</div>
  	
  	<form action="" method="post" id="form1">
			<input type="hidden" id="x" name="x" />
			<input type="hidden" id="y" name="y" />
			<input type="hidden" id="w" name="w" />
			<input type="hidden" id="h" name="h" />
			<input type="hidden" value="cut" name="act" />
			<input type="hidden" id="src" name="src" />
			<input type="hidden" id="dest" name="dest" />
	</form>
</body>
</html>
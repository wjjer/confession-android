<!DOCTYPE html>
<html>
<head>
<title>首页</title>
<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <style>
        body {
            margin: 0;
            padding: 0;
        }
    </style>
<style type="text/css">

		.btn { /* 按钮美化 */
			width: 270px; /* 宽度 */
			height: 40px; /* 高度 */
			border-width: 0px; /* 边框宽度 */
			border-radius: 3px; /* 边框半径 */
			background: #1E90FF; /* 背景颜色 */
			cursor: pointer; /* 鼠标移入按钮范围时出现手势 */
			outline: none; /* 不显示轮廓线 */
			font-family: Microsoft YaHei; /* 设置字体 */
			color: white; /* 字体颜色 */
			font-size: 17px; /* 字体大小 */
		}
		.btn:hover { /* 鼠标移入按钮范围时改变颜色 */
			background: #5599FF;
		}

</style>

<script type="text/javascript" src="../../jquery.min.js"></script>
 <script>
 var current = 0
 var urlStr = '${_urls_}'
 var urls = urlStr.split(',')
    $(document).ready(function(){
        //高度设置
        //var height = $(window).height();
		var height =  window.innerHeight;
		 
        $('#myiframe').height(height);
		//$('#myiframe').attr('src',urls[0])
		document.getElementById('myiframe').src = urls[0];
		 
    })
    function changeFrameHeight(){
	    var height =  window.innerHeight;
        var ifm= document.getElementById("myiframe");
        $('#myiframe').height(height);
    }
    window.onresize=function(){
        changeFrameHeight();
    }

function test(){
	if(current == -1){
		$('#next').attr('value','NEXT →')
	}
	current++
	
	if(current+1 == urls.length-1){
		document.getElementById('myiframe').src = urls[current];
		$('#next').attr('value','重新观看')
		current = -1
		return
	}
	
	console.log('c: '+ current + '  l: '+ urls.length + '  u: '+ urls[current])
	document.getElementById('myiframe').src = urls[current];
}	
</script>

</head>
<body>
<div style="z-index:999;position:fixed;bottom: 50px;right: 50px; background: #b4d145;" >
		<input id="next" type="button" class = "btn" value="NEXT →" onclick="test()"/>
</div>
<iframe src="" id="myiframe" scrolling="false" frameborder="0" width="100%"></iframe>
	
</body>
</html>
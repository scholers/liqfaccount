/*
Script Name:图片新闻滚动(渐变)
Site:http://www.vfresh.cn/
E-mail:lzwdpc@163.com
*/
/*--------------- 使用到的公用函数 ---------------*/

/** 获取id值为eleID的对象*/
function $id(eleID) {
	return document.getElementById(eleID);
}
/** 创建ele的父节点newprarent
 * @param newparent
 * @param ele
 */
function wrap(newparent,ele) {
	if(ele.nextSibling) {
		var n = ele.nextSibling;
		n.parentNode.insertBefore(newparent,n);
	}else if(ele.previousSibling) {
		insertAfter(newparent,ele.previousSibling);
	}else {
		ele.parentNode.appendChild(newparent);
	}
	newparent.appendChild(ele);
}
/** 将fn绑定到tar的ev事件
 * @param tar
 * @param ev
 * @param fn
 */
function addEvent(tar,ev,fn) {
	if(document.attachEvent) {
		tar.attachEvent('on'+ev,fn);
	}else if(document.addEventListener) {
		tar.addEventListener(ev,fn,false);
	}
}
/** 移除tar上ev事件里的fn
 * @param tar
 * @param ev
 * @param fn
 */
function removeEvent(tar,ev,fn) {
	if(document.detachEvent) {
		tar.detachEvent('on'+ev,fn);
	}else if(document.removeEventListener) {
		tar.removeEventListener(ev,fn,false);
	}
}
/** 在tarElement之后插入newElement对象
 * @param newElement 要被插入文档结构的对象。
 * @param targetElement 指定插入点。
 */
function insertAfter(newElement,tarElement) {
	var pnode = tarElement.parentNode;
	if (pnode.lastChild == tarElement) {
		pnode.appendChild(newElement);
	} else {
		pnode.insertBefore(newElement,tarElement.nextSibling);
	}
}
/** 将element插入到tarElement的前面
 * @param element
 * @param tarElement
 */
function prepend(element,tarElement) {
	if(tarElement.firstChild) {
		tarElement.insertBefore(element,tarElement.firstChild);
	}else {
		tarElement.appendChild(element);
	}
}
/** 获取ele的样式属性“value”的值
 * @param ele
 * @param attr
 */
function getStyle(ele,attr) {
	var rs;
	if(ele.style[attr]) {
		rs = ele.style[attr];
	}else if(window.getComputedStyle) {
		attr=attr.replace(/([A-Z])/g,"-$1");
		//value=value.toLowerCase();
		rs = window.getComputedStyle(ele,'').getPropertyValue(attr);
		if(attr == 'color') {rs = colorToHex(rs);} //格式化color为16进制表示
	}else if(ele.currentStyle) {
		rs = ele.currentStyle[attr];
	}else {
		return null;
	}
	return rs;
}
/** 获取ele的整体高度（height+padding+margin+borde），返回一个数值
 * @param ele
 */
function getTotalHeight(ele) {
	var rs = (ele.offsetHeight||0) + (parseInt(getStyle(ele,'marginTop'))||0) + (parseInt(getStyle(ele,'marginBottom'))||0);
	return rs;
}
/** 获取ele的整体宽度（width+padding+margin+border），返回一个数值
 * @param ele
 */
function getTotalWidth(ele) {
	var rs = (ele.offsetWidth||0) + (parseInt(getStyle(ele,'marginLeft')) || 0) + (parseInt(getStyle(ele,'marginRight')) || 0);
	return rs;
}

/** 特效显示ele
 * @param ele
 * @param mode 可选项。进行显示的样式，默认为渐隐显示；默认速度80
 *				 (mode == 'spread' || mode== 1)：垂直向下伸展；默认速度20
 *				 (mode == 'rightzoom' || mode == 2)：向右扩展显示；//未完成
 * @param time 可选项。渐隐显示的速度，单位毫秒，数值越大速度越慢
 */
function showSlow(ele,mode,time) {
	if(ele.vvshowSlow) {clearInterval(ele.vvshowSlow);}
	/** 渐隐
	 * @ignore
	 */
	this.showAlpha = function () {
		if(ele.filters) {
			if(ele.filters.alpha.opacity == 100) {clearInterval(ele.vvshowSlow);return false;}
			ele.filters.alpha.opacity += 5;
		}else if(ele.style.opacity) {
			if(ele.style.opacity == '1') {clearInterval(ele.vvshowSlow);return false;}
			ele.style.opacity = parseFloat(ele.style.opacity)+0.05;
		}
	}
	/** 垂直向下
	 * @ignore
	 */
	this.showSpread = function () {
		if(parseInt(getStyle(ele,'paddingTop')) < ele.vvshowSlow_PT) {
			ele.style.paddingTop = (parseInt(ele.style.paddingTop)+1) +'px';
		}else if(parseInt(getStyle(ele,'height')) < ele.vvshowSlow_TH) {
			ele.style.height = (parseInt(ele.style.height)+1) +'px';
		}else if(parseInt(getStyle(ele,'paddingBottom')) < ele.vvshowSlow_PB) {
			ele.style.paddingBottom = (parseInt(ele.style.paddingBottom)+1) +'px';
		}else {
			clearInterval(ele.vvshowSlow);
			with(ele.style) {//还原
				paddingTop = ele.vvshowSlow_PT;
				paddingBottom = ele.vvshowSlow_PB;
				height = ele.vvshowSlow_H;
				overflow = ele.vvshowSlow_OF;
			}
			return false;
		}
	}
	/** 向右(下)扩张
	 * @ignore
	 */
	this.rightzoom = function () {
		if((parseInt(getStyle(ele,'paddingTop')) < ele.vvshowSlow_PT) || (parseInt(getStyle(ele,'paddingLeft')) < ele.vvshowSlow_PL)) {
			if(parseInt(getStyle(ele,'paddingTop')) < ele.vvshowSlow_PT) {ele.style.paddingTop = (parseInt(ele.style.paddingTop)+1) +'px';}
			if(parseInt(getStyle(ele,'paddingLeft')) < ele.vvshowSlow_PL) {ele.style.paddingLeft = (parseInt(ele.style.paddingLeft)+1) +'px';}
		}else if((parseInt(getStyle(ele,'height')) < ele.vvshowSlow_TH)||(parseInt(getStyle(ele,'width')) < ele.vvshowSlow_TW)) {
			if(parseInt(getStyle(ele,'height')) < ele.vvshowSlow_TH) {ele.style.height = (parseInt(ele.style.height)+1) +'px';}
			if(parseInt(getStyle(ele,'width')) < ele.vvshowSlow_TW) {ele.style.height = (parseInt(ele.style.width)+1) +'px';}
		}else if(parseInt(getStyle(ele,'paddingBottom')) < ele.vvshowSlow_PB) {
			ele.style.paddingBottom = (parseInt(ele.style.paddingBottom)+1) +'px';
		}else {
			clearInterval(ele.vvshowSlow);
			with(ele.style) {//还原
				paddingTop = ele.vvshowSlow_PT;
				paddingBottom = ele.vvshowSlow_PB;
				height = ele.vvshowSlow_H;
				overflow = ele.vvshowSlow_OF;
			}
			return false;
		}
	}
	if(mode == 'spread' || mode== 1) {//垂直向下伸展
		ele.style.zoom = ele.style.zoom||'1';//修正IE layout bug
		if(!ele.vvshowSlow) {
			ele.vvshowSlow_PT = parseInt(getStyle(ele,'paddingTop'))|| 0;
			ele.vvshowSlow_PB = parseInt(getStyle(ele,'paddingBottom'))||0;
			ele.vvshowSlow_H = getStyle(ele,'height') || 'auto';
			ele.vvshowSlow_TH = ele.clientHeight-ele.vvshowSlow_PT-ele.vvshowSlow_PB;
			ele.vvshowSlow_OF = getStyle(ele,'overflow');

			with(ele.style) {//初始化
				height = '0';
				paddingTop='0';
				paddingBottom='0';
				overflow = 'hidden';
			}
		}
		ele.vvshowSlow = setInterval(this.showSpread,time||20);
	}else if(mode == 'rightzoom' || mode == 2) {//向右扩展
		ele.style.zoom = ele.style.zoom||'1';//修正IE layout bug
		if(!ele.vvshowSlow) {
			ele.vvshowSlow_PL = parseInt(getStyle(ele,'paddingLeft'))|| 0;
			ele.vvshowSlow_PR = parseInt(getStyle(ele,'paddingRight'))||0;
			ele.vvshowSlow_PT = parseInt(getStyle(ele,'paddingTop'))|| 0;
			ele.vvshowSlow_PB = parseInt(getStyle(ele,'paddingBottom'))||0;
			ele.vvshowSlow_H = getStyle(ele,'height') || 'auto';
			ele.vvshowSlow_TW = ele.clientWidth-ele.vvshowSlow_PL-ele.vvshowSlow_PR;
			ele.vvshowSlow_TH = ele.clientHeight-ele.vvshowSlow_PT-ele.vvshowSlow_PB;
			ele.vvshowSlow_OF = getStyle(ele,'overflow');

			with(ele.style) {//初始化
				height = '0';
				width = '0';
				paddingLeft = '0';
				paddingRight = '0';
				paddingTop='0';
				paddingBottom='0';
				overflow = 'hidden';
			}
		}
		ele.vvshowSlow = setInterval(this.rightzoom,time||20);
	}else {//默认渐隐
		with(ele.style) {
			if(display == 'none') {display = ''}
			if(visibility == 'hidden') {visibility = ''}
			if(ele.filters) {// IE
				zoom = zoom || '1'; //修正 IE 子节点无法继承父节点alpha的问题
				if(!ele.filters.alpha) {filter += 'alpha(opacity=0)';}
			}else if(opacity != null) {// 其他标准浏览器
				opacity = 0;
			}else {
				return false;
			}
		}
		ele.vvshowSlow = setInterval(this.showAlpha,time||80);
	}
}
/*--------------- 使用到的公用函数 END ---------------*/


function IMGSCROLL() {
	var self = this;
	this.sn = 0;
	this.pause = false;

	//this.imgWrap = $id('imgWrap'); //图片的外包层
	//this.w = '';//图片宽度，不需要单位px
	//this.h = '';//图片高度，不需要单位px
	//this.blankPic = 'blank.gif';//透明图片地址
	this.img = $id('IMG'); //默认显示的图片元素
	this.btnClass = 'scrollNewsBtn'; //按钮外包class
	this.btnLightClass = 'lh';//按钮高亮class
	this.imgSrc = [];//图片路径数组
	this.urls = []; //连接数组,“#”代表无连接
	this.time = 3500; //轮转时间，毫秒

	this.preImg = document.createElement('img');//创建切换的图片
	this.img = document.createElement('img');
		this.img.style.position = 'relative';
		this.img.style.zIndex = '99';
	this.link = document.createElement('a');
		this.link.setAttribute('target','_blank');
	this.btnWrap = document.createElement('span');
		this.btnWrap.className = self.btnClass;
		this.btnWrap.style.zIndex = '100';

	this.scroll = function (e) {//滚动切换
		var l = self.imgSrc.length -1;
		if(self.pause && e !='btnEvent') {return false;}
		var sn = self.sn;
		if(self.img.filters) {// IE
			self.img.filters.blendTrans.apply();
			self.img.filters.blendTrans.play();
			self.img.setAttribute('src',self.imgSrc[sn]);
		}else { //其他标准浏览器
			self.preImg.setAttribute('src',self.img.getAttribute('src'));
			self.img.setAttribute('src',self.imgSrc[sn]);
			showSlow(self.img,0,60);
		}
		var newhref = self.urls[sn];

		var btnlinks = self.btnWrap.getElementsByTagName('a');
		var btnlinksL= btnlinks.length;
		for(var i=0; i<btnlinksL; i++) {
			btnlinks[i].className = '';
		}
		if(btnlinksL>0) {
			btnlinks[sn].className = self.btnLightClass;
		}

		if(newhref != '#' && newhref != null && newhref != '') {
			self.link.setAttribute('href',newhref);
			if(btnlinksL>0){
				btnlinks[sn].setAttribute('href',newhref);
				btnlinks[sn].onclick = function () {return true;};
			}
		}else {
			self.link.removeAttribute('href');
		}

		if(self.sn == l) {
			self.sn = 0;
		}else {
			self.sn++;
		}
	}
	this.createBtn = function (targetwrap) {//创建按钮
		var l = self.imgSrc.length -1;
		var btns = [];
		for(var i=1; i<=(l+1); i++) {
			btns.push('<a href="#" target="_blank" onclick="this.blur();return false;">'+i+'</a>');
		}
		self.btnWrap.innerHTML = btns.join('');
		targetwrap.appendChild(self.btnWrap);
		var btnlinks = self.btnWrap.getElementsByTagName('a');
		for(var i=0; i<btnlinks.length; i++) {
			btnlinks[i].num = i;
			btnlinks[i].onmouseover = function () {self.btnEventOn(this,this.num);}
			btnlinks[i].onmouseout = function () {self.btnEventOff(this,this.num);}
		}
	}
	this.btnEventOn = function (ele,num) {
		self.pause = true;
		ele.className = self.btnLightClass;
		self.sn = num;
		self.scroll('btnEvent');
	}
	this.btnEventOff = function (ele,num) {
		self.pause = false;
	}
	this.start = function () {
		if(!self.imgWrap || $id('vvImgScrollWrap_'+self.imgWrap.getAttribute('id'))) {return false;}
		clearInterval(self.img.vvshowScroll);
		self.img.setAttribute('src',self.blankPic);
		(self.imgWrap.parentNode).insertBefore(self.img,self.imgWrap);
		var h = self.h=='auto'?'auto':(self.h + 'px');
		var w = self.w=='auto'?'auto':(self.w + 'px');
		if(self.img.filters) {
			(self.img).style.filter = 'blendTrans(duration=1)';
		}
		with(self.img.style) {
			height = h || 'auto';
			width = w || 'auto';
		}
		var pwrap = document.createElement('div');//创建外包层
			pwrap.setAttribute('id','vvImgScrollWrap_'+self.imgWrap.getAttribute('id'));
			with(pwrap.style) {
				position = 'relative';
				width = w;
				height = h;
			}
		wrap(self.link,self.img);
		wrap(pwrap,self.link);
		self.createBtn(pwrap);//生成按钮

		self.preImg.setAttribute('src',self.blankPic);//切换的图片
		with(self.preImg.style) {
			position = 'absolute';
			top = '0';
			left = '0';
			width = w;
			height = h;
		}
		(self.img.parentNode).insertBefore(self.preImg,self.img);

		self.img.onmouseover = function () {
			self.pause = true;
		}
		self.img.onmouseout = function () {
			self.pause = false;
		}
		self.scroll();
		self.img.vvshowScroll = setInterval(self.scroll,self.time);
	}
}
function imgScroll(imgWrap,time,w,h,btnclass,btnlight) {
	if(!imgWrap) {alert('imgWrap没找到');return false;}
	var imgs = imgWrap.getElementsByTagName('img');
	var links = [],srcs = [],hrefs=[];
	var pa;
	for(var i=0,l=imgs.length; i<l; i++) {
		srcs.push(imgs[i].getAttribute('src'));
		pa = imgs[i].parentNode;
		if(pa.nodeName == 'A') {
			hrefs.push(pa.getAttribute('href'));
		}else {
			hrefs.push('#');
		}
	}

	var scrollnews = new IMGSCROLL();
	scrollnews.imgWrap = imgWrap;
	scrollnews.blankPic = 'blank.gif'; //透明图层的路径，请自行修改
	scrollnews.imgSrc = srcs;
	scrollnews.urls = hrefs;
	scrollnews.time = time || 3500;
	scrollnews.w = w || 'auto';
	scrollnews.h = h || 'auto';
	scrollnews.btnClass = btnclass || 'scrollNewsBtn';
	scrollnews.btnLightClass = btnlight || 'lh';
	scrollnews.start();
}
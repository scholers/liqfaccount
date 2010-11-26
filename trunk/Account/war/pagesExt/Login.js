Ext.onReady(function(){
     Ext.BLANK_IMAGE_URL ="scripts/ext/resources/images/default/s.gif";      
     Ext.QuickTips.init();
     Ext.form.Field.prototype.msgTarget="qtip"; 
    
      var loginForm = new Ext.FormPanel({
          renderTo:"loginForm",
          id:"loginForm1",    
          width:340,
          height:155,  
          frame:true,
          monitorValid:true,
          items:[  
               new Ext.form.TextField({  
               id:"username",
               name:"username",
               fieldLabel:"用 户 名",
               minLength:"6",
               minLengthText:"用户名长度不能小于{0}",
               maxLength:"20",
               maxLengthText:"用户名长度不能大于{0}",
               allowBlank:false,
               blankText:"必须输入用户名"
              }),  
              new Ext.form.TextField({
               id:"password",  
               name:"password",
               fieldLabel:"密　　码",
               inputType:"password",
               minLength:"6",
               minLengthText:"密码长度不能小于{0}",
               maxLength:"20",
               maxLengthText:"密码长度不能大于{0}",
               allowBlank:false,
               blankText:"必须输入密码"
              }),
             
              new Ext.form.TextField({
              id:"randCode",
              name:"randCode",
              width:"70",
              fieldLabel:"验证码",
              allowBlank:false,
              blankText:"必须输入验证码"
              })
              ],
          buttons:[
                {text:"登录",formBind:true,handler:function(){
                    loginForm.getForm().submit({
	                    url:"bookext.do?method=login", 
	                    waitMsg:"请稍等,正在登录!",     
                        success:function(form ,action){   
                           // Ext.Msg.alert("用户名",action.result.user);    
                            window.location.href="/MyWebRebuild/bookext.do?method=getUserName";
                        },   
                        failure:function(form , action){       
                            Ext.Msg.alert("提示",action.result.msg);
                        }    
                    });
                    }},         
                
                {text:"重置",handler:function(){
                    loginForm.form.reset();
                    }}
          ]    
          });  
       
          var rc =Ext.getDom("randcode");
          var rcp =Ext.get(rc.parentNode);
          rcp.createChild({tag:'img',src:'pagesExt/image.jsp',align:'absbottom'}); 
          var loginWindow = new Ext.Window({
          width:340,
          height:200,
          title:"用户登录",
          items:loginForm,
          draggable:true,
          resizable:false,
          closable:true
          });
          loginWindow.show();
    });
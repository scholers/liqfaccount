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
               fieldLabel:"�� �� ��",
               minLength:"6",
               minLengthText:"�û������Ȳ���С��{0}",
               maxLength:"20",
               maxLengthText:"�û������Ȳ��ܴ���{0}",
               allowBlank:false,
               blankText:"���������û���"
              }),  
              new Ext.form.TextField({
               id:"password",  
               name:"password",
               fieldLabel:"�ܡ�����",
               inputType:"password",
               minLength:"6",
               minLengthText:"���볤�Ȳ���С��{0}",
               maxLength:"20",
               maxLengthText:"���볤�Ȳ��ܴ���{0}",
               allowBlank:false,
               blankText:"������������"
              }),
             
              new Ext.form.TextField({
              id:"randCode",
              name:"randCode",
              width:"70",
              fieldLabel:"��֤��",
              allowBlank:false,
              blankText:"����������֤��"
              })
              ],
          buttons:[
                {text:"��¼",formBind:true,handler:function(){
                    loginForm.getForm().submit({
	                    url:"bookext.do?method=login", 
	                    waitMsg:"���Ե�,���ڵ�¼!",     
                        success:function(form ,action){   
                           // Ext.Msg.alert("�û���",action.result.user);    
                            window.location.href="/MyWebRebuild/bookext.do?method=getUserName";
                        },   
                        failure:function(form , action){       
                            Ext.Msg.alert("��ʾ",action.result.msg);
                        }    
                    });
                    }},         
                
                {text:"����",handler:function(){
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
          title:"�û���¼",
          items:loginForm,
          draggable:true,
          resizable:false,
          closable:true
          });
          loginWindow.show();
    });
webpackJsonp([20],{GvYv:function(s,e){},"gf/L":function(s,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var t=a("Dd8w"),r=a.n(t),i=a("NYxO"),o=a("+cKO"),d=a("TYx6"),n=a("i53X"),c=a("a2KH"),l=a("/QaM"),u=a("IcnI"),m=(a("mtWM"),{name:"ShiftPasssword",components:{PasswordField:d.a,PasswordRepeatField:n.a,NumberField:c.a,EmailField:l.a},data:function(){return{email:"",password:"",passwordTwo:"",code:3675,number:""}},computed:r()({},Object(i.c)(["getCode"])),methods:r()({},Object(i.b)("profile/account",["passwordSet"]),{submitHandler:function(){var s=this;this.$v.$invalid?this.$v.$touch():this.passwordSet(this.passwordTwo).then(function(){s.$router.push({name:"ChangePasswordSuccess"})})}}),mounted:function(){this.code=this.getCode},validations:{email:{required:o.required,email:o.email},password:{required:o.required,minLength:Object(o.minLength)(8)},passwordTwo:{required:o.required,minLength:Object(o.minLength)(8),sameAsPassword:Object(o.sameAs)("password")},number:{required:o.required,numeric:o.numeric,isCode:function(s){return+s===u.a.state.code}}}}),w={render:function(){var s=this,e=s.$createElement,a=s._self._c||e;return a("div",{staticClass:"shift-password"},[a("form",{staticClass:"shift-password__form",on:{submit:function(e){return e.preventDefault(),s.submitHandler(e)}}},[a("div",{staticClass:"form__block"},[a("h4",{staticClass:"form__subtitle"},[s._v("Смена пароля")]),a("email-field",{class:{checked:s.$v.email.required&&s.$v.email.email},attrs:{id:"shift-email",v:s.$v.email},model:{value:s.email,callback:function(e){s.email=e},expression:"email"}}),a("password-field",{class:{checked:s.$v.password.required&&s.$v.passwordTwo.sameAsPassword&&s.$v.password.minLength},attrs:{id:"shift-password",v:s.$v.password,info:"info",registration:"registration"},model:{value:s.password,callback:function(e){s.password=e},expression:"password"}}),a("password-repeat-field",{class:{checked:s.$v.password.required&&s.$v.passwordTwo.sameAsPassword&&s.$v.passwordTwo.minLength},attrs:{id:"shift-repeat-password",v:s.$v.passwordTwo},model:{value:s.passwordTwo,callback:function(e){s.passwordTwo=e},expression:"passwordTwo"}})],1),a("div",{staticClass:"form__block"},[a("h4",{staticClass:"form__subtitle"},[s._v("Введите код")]),a("span",{staticClass:"form__code"},[s._v(s._s(s.code))]),a("number-field",{class:{checked:s.$v.number.required&&s.$v.number.isCode},attrs:{id:"shift-number",v:s.$v.number},model:{value:s.number,callback:function(e){s.number=e},expression:"number"}})],1),a("div",{staticClass:"shift-password__action"},[a("button-hover",{attrs:{tag:"button",type:"submit",variant:"white"}},[s._v("Сменить")])],1)])])},staticRenderFns:[]};var p=a("VU/8")(m,w,!1,function(s){a("GvYv")},null,null);e.default=p.exports}});
//# sourceMappingURL=20.7264a605675ccbfcd0a9.js.map
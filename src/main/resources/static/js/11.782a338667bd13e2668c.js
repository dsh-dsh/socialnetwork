webpackJsonp([11],{"3qdu":function(e,n,a){"use strict";Object.defineProperty(n,"__esModule",{value:!0});var i=a("fA45"),t=a("+Xkk"),s=a("JPen"),c={name:"AdminComments",components:{AdminSidebar:t.a,AdminSearch:i.a,CommentsBlock:s.a},data:function(){return{filter:"all",search:""}},methods:{onChangeFilter:function(e){this.filter=e},onChangeSearch:function(e){this.search=e}}},r={render:function(){var e=this,n=e.$createElement,a=e._self._c||n;return a("div",{staticClass:"admin-comments inner-page admin"},[a("h2",{staticClass:"admin__title"},[e._v("Комментарии")]),a("div",{staticClass:"admin__wrap"},[a("div",{staticClass:"inner-page__main"},[a("div",{staticClass:"admin__search"},[a("admin-search",{on:{"change-value":e.onChangeSearch},model:{value:e.search,callback:function(n){e.search=n},expression:"search"}})],1),a("div",{staticClass:"admin-comments__list"},[a("comments-block",{attrs:{admin:"admin"}}),a("comments-block",{attrs:{admin:"admin",blocked:"blocked"}})],1)]),a("div",{staticClass:"inner-page__aside"},[a("admin-sidebar",{on:{"change-filter":e.onChangeFilter},model:{value:e.filter,callback:function(n){e.filter=n},expression:"filter"}})],1)])])},staticRenderFns:[]};var l=a("VU/8")(c,r,!1,function(e){a("n76m")},null,null);n.default=l.exports},n76m:function(e,n){}});
//# sourceMappingURL=11.782a338667bd13e2668c.js.map
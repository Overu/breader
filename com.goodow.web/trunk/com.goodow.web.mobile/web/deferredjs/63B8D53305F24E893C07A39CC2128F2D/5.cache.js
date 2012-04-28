function wNb(a){this.b=a}
function PNb(a){pJb();this.b=a}
function FNb(a,b){this.b=a;this.c=b}
function ANb(a,b){pJb();this.b=a;this.c=b}
function KNb(a,b){pJb();this.b=a;this.c=b}
function ENb(a,b){pNb(a.b.b,b)}
function JNb(a,b){pNb(a.b,b)}
function YNb(a,b){a.b.n=b;a.b.k=a.d.Fh()-1}
function X$b(a,b){this.b=b;Vyb.call(this,a)}
function I_b(a,b){this.b=b;Vyb.call(this,a)}
function g0b(a,b){this.b=b;Vyb.call(this,a)}
function ZNb(a,b,c){pJb();this.b=a;this.d=b;this.c=c}
function UNb(a,b,c,d){pJb();this.b=a;this.c=b;this.d=c;this.e=d}
function mNb(a,b,c){rJb(tJb(new UNb(a,true,c,b),b.Sf(),sW.i))}
function zNb(a,b){rJb(wJb(new FNb(a,b),a.c,sW.i))}
function kNb(){kNb=bpc;jNb=(T2(),G2(HU.i))}
function W$b(a,b){return uub(a.f,new abc(b)),a}
function f0b(a,b){return uub(a.f,new abc(b)),a}
function klc(a,b){return !a.f?null:PG(bBb(a.f,b),119)}
function R$b(a,b){var c;c=new X$b(a,b);Yzb(a.f.d,c);return c}
function B_b(a,b){var c;c=new I_b(a,b);Yzb(a.f.d,c);return c}
function $_b(a,b){var c;c=new g0b(a,b);Yzb(a.f.d,c);return c}
function lNb(a,b){R3(a.i,qlc(rlc(Cgc(a.g.b),HU.i),GG(W_,xpc,119,[b.Sf()])))}
function TNb(a,b){a.b.f=b;a.c&&(a.d?lNb(a.b,PG(b.He(0),150)):lNb(a.b,PG(b.He(b.Pe()-1),150)))}
function ONb(a,b){vg(id(a.b),a.b.j.jh());Vib(a.b.c,b.xh());noc(a.b.o,goc(a.b.d,a.b.j.wh().Ch().Sf(),dyc),a.b.j)}
function hz(a){var b,c;b=a.c;if(b){return c=a.b,(c.clientX||0)-Fs(b)+Hs(b)+Hs(b.ownerDocument.body)}return a.b.clientX||0}
function qNb(a,b,c,d,e){kNb();kd.call(this);this.i=a;this.b=b;this.g=c;this.o=d;this.d=e;this.c=new Wib;jd(this,this.c);jc(this.c,new wNb(this),(kz(),kz(),jz))}
function pNb(a,b){var c,d;d=b.wh();c=d.Ch();a.j=b;rJb(wJb(new PNb(a),a.j.Sf(),wW.i));rJb(tJb(new ZNb(a,d,b),c.Sf(),AW.i));rJb(tJb(new UNb(a,false,false,d),d.Sf(),sW.i))}
function oNb(a,b,c){if(b>a.n.Pe()-1&&a.e+2>a.f.Pe()){a.n.Pe()-1;Y2(jNb,'\u6700\u540E\u4E00\u9875');return}else if(b<0){Y2(jNb,'\u7B2C\u4E00\u9875');return}mNb(a,PG(a.n.He(b),152),c)}
function nNb(a,b){var c,d;a.e=a.f.Ie(a.j);c=a.e+b;if(c<0){oNb(a,--a.k,false);return}else if(c+1>a.f.Pe()){oNb(a,++a.k,true);return}d=PG(a.f.He(c),150);R3(a.i,qlc(rlc(Cgc(a.g.b),HU.i),GG(W_,xpc,119,[d.Sf()])))}
function Qgc(a){!a.C&&(a.C=new qNb((!a.k&&(a.k=hhc(new Nlc,(!a.q&&(a.q=new AB),a.q))),a.k),(!a.N&&(a.N=(!a.M&&(a.M=agc(new gMb,(!a.q&&(a.q=new AB),a.q),(!a.u&&(a.u=(!a.W&&(a.W=Dgc(a)),a.W)),a.u))),a.M)),a.N),new Sic(a),(!a.Z&&(a.Z=Ggc(a)),a.Z),(!a.Y&&(a.Y=Fgc(a)),a.Y)));return a.C}
var dyc='now_reader';i1(935,29,Mqc,qNb);_.rc=function rNb(){return null};_.sc=function sNb(){};_.tc=function tNb(){};_.uc=function uNb(a,b){var c,d,e;c=klc(PG(this.i.c,199),mW);if(c){e=PG(loc(this.o,goc(this.d,c,dyc)),150);if(e){R3(this.i,qlc(rlc(Cgc(this.g.b),HU.i),GG(W_,xpc,119,[e.Sf()])));return}rJb(vJb(new ANb(this,c),c));return}d=klc(PG(this.i.c,199),sW);rJb(vJb(new KNb(this,d),d))};_.b=null;_.c=null;_.d=null;_.e=0;_.f=null;_.g=null;_.i=null;_.j=null;_.k=0;_.n=null;_.o=null;var jNb;i1(936,1,npc,wNb);_.Jb=function xNb(a){var b,c;b=a.c.offsetWidth||0;c=hz(a);nNb(this.b,c>~~(b/2)?1:-1)};_.b=null;i1(937,870,wqc,ANb);_.eh=function BNb(a){zNb(this,PG(a,148))};_.fh=function CNb(){return _wb(this.b.b,this.c)};_.b=null;_.c=null;i1(938,870,wqc,FNb);_.eh=function GNb(a){ENb(this,PG(a,150))};_.fh=function HNb(){return W$b(R$b(new T$b(this.b.b.b),this.c),GG(h0,ppc,1,[eyc]))};_.b=null;_.c=null;i1(939,870,wqc,KNb);_.eh=function LNb(a){JNb(this,PG(a,150))};_.fh=function MNb(){return Uyb(_wb(this.b.b,this.c),GG(h0,ppc,1,[eyc]))};_.b=null;_.c=null;i1(940,870,wqc,PNb);_.eh=function QNb(a){ONb(this,PG(a,151))};_.fh=function RNb(){return B_b(new C_b(this.b.b),this.b.j)};_.b=null;i1(941,870,wqc,UNb);_.eh=function VNb(a){TNb(this,PG(a,188))};_.fh=function WNb(){return $$b(S$b(new T$b(this.b.b),this.e),GG(h0,ppc,1,[eyc]))};_.b=null;_.c=false;_.d=false;_.e=null;i1(942,870,wqc,ZNb);_.eh=function $Nb(a){YNb(this,PG(a,188))};_.fh=function _Nb(){return f0b($_b(new __b(this.b.b),this.c),GG(h0,ppc,1,[pvc]))};_.b=null;_.c=null;_.d=null;i1(1111,797,zqc,X$b);_.$f=function Y$b(){return new VBb('YwwghmRWh3z2ISEsQWnilsnutNA=',GG(e0,xpc,0,[this.b]),this.f,sW,null)};_.b=null;i1(1118,797,zqc,I_b);_.$f=function J_b(){return new VBb('8cf5TE7b6ZLk3kZ7mPuOw3fRdQw=',GG(e0,xpc,0,[this.b]),this.f,wW,null)};_.b=null;i1(1122,797,zqc,g0b);_.$f=function h0b(){return new VBb('oJf4CLfaLwYdk_lui7rncFffxec=',GG(e0,xpc,0,[this.b]),this.f,cZ,AW)};_.b=null;i1(1291,1,src);_.Pc=function okc(){alc(this.c,Qgc(this.b.b))};var AU=_4b(Ewc,'ContentEditor$1',936,ZX),CU=_4b(Ewc,'ContentEditor$2',937,q_),BU=_4b(Ewc,'ContentEditor$2$1',938,q_),DU=_4b(Ewc,'ContentEditor$3',939,q_),EU=_4b(Ewc,'ContentEditor$4',940,q_),FU=_4b(Ewc,'ContentEditor$5',941,q_),GU=_4b(Ewc,'ContentEditor$6',942,q_),ZW=_4b(vxc,'ResourceContextImpl$4X',1118,MS),SW=_4b(vxc,'PageContextImpl$2X',1111,MS),bX=_4b(vxc,'SectionContextImpl$3X',1122,MS);urc(wo)(5);
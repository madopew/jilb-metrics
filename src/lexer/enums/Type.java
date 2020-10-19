package lexer.enums;

public enum Type {
    PUNC, // ( ) { } ;
    NUM, //123
    STR, // "adsadsa"
    CHAR, // 'a'
    HKW, //for while final
    SKW,
    OP, //+ ++ %=
    VAR //myAmount, var
}

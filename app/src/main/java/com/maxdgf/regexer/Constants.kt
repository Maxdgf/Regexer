package com.maxdgf.regexer

const val REGEXER_APP_GITHUB_REPO_LINK = "https://github.com/Maxdgf/Regexer"
const val REGEX_CHEAT_SHEET_TEXT = "Character classes:\n" +
        ".\tany character except newline\n" +
        "\\w \\d \\s\tword, digit, whitespace\n" +
        "\\W \\D \\S\tnot word, digit, whitespace\n" +
        "[abc]\tany of a, b, or c\n" +
        "[^abc]\tnot a, b, or c\n" +
        "[a-g]\tcharacter between a & g\n" +
        "\n" +
        "Anchors:\n" +
        "^abc$\tstart / end of the string\n" +
        "\\b\tword boundary\n" +
        "\n" +
        "Escaped characters:\n" +
        "\\. \\* \\\\\tescaped special characters\n" +
        "\\t \\n \\r\t tab, linefeed, carriage return\n" +
        "\\u00A9\tunicode escaped ©\n" +
        "\n" +
        "Groups & Lookaround:\n" +
        "(abc)\tcapture group\n" +
        "\\1\tbackreference to group #1\n" +
        "(?:abc)\tnon-capturing group\n" +
        "(?=abc)\tpositive lookahead\n" +
        "(?!abc)\tnegative lookahead\n" +
        "\n" +
        "Quantifiers & Alternation:\n" +
        "a* a+ a?\t0 or more, 1 or more, 0 or 1\n" +
        "a{5} a{2,}\texactly five, two or more\n" +
        "a{1,3}\tbetween one & three\n" +
        "a+? a{2,}?\tmatch as few as possible\n" +
        "ab|cd\tmatch ab or cd"

const val REGEXER_APP_INFO = "This is a simple application with a convenient and clear interface for testing your regular expression patterns."
const val OPEN_SOURCE_PROJECT_DESCRIPTION = "This application is open source, for more details you can visit the link below:"
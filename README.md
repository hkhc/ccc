# Chinese Commercial Code mapping library

## Setup

```gradle
    dependencies {
        implementation 'io.hkhc.ccc:ccc:1.0'
    }
```

### Introduction

Before the computer-era, people send textual message all over the world with
telegraph. It is straight forward to send messages in English alphabets. Not so
for Chinese characters. People invented a coding system to encode most frequently
used Chinese characters in 4-digit code. It is known as Chinese Commercial Code. It is not friendly for data input, but very
space efficient, which is far more valuable in the old days.

Nowaday we have better coding system for Chinese characters, as part of Unicode
system. However the Chinese Commercial Code system is still in use today, to specify
the legal name of people in Hong Kong Identity Card (HKID) and passport. The corresponding Chinese
Commercial Code of the Chinese name is printed on the documents. It helps non-Chinese
law enforcement agencies to recognize the Chinese name of people on the documents. It also
help HKID data collections with camera by recognize digits rather than Chinese characters.

However, mapping between Chinese characters and Chinese Commercial Codes is not a commmon facility of modern
programming platform. This library fills the gap for applications that have this needs.

Note that mapping between Chinese characters and Chinese Commercial Code is not one-to-one. One code
may represents different Chinese characters, and it is not entirely rare. Most of the time the traditional Chinese
version and the Simplified Chinese version of the same character are mapped to the same code. So it is not practical to 
map the code back to Chinese characters mechanically. But it is mostly good enough for person name identificaation purpose. 
It is also possible that a Chinese character (available in Unicode) and no mapping in Chinese Commercial Code.
There are much more characters for 4-digit coding system. 

## Examples

```java
        CCCDB db = new CCCDB();
        db.load();
        Assert.assertEquals("7115 1129 2429", db.getCCCs("?????????"));
```

If the Chinese characters has no corresponding Chinese Commercial Code, the result would be 
"0000'.

## Source data

Source data for the mapping files are placed under data folder. The encoding mapping file
is generated by `CCCGenerator` class.

## Building the library

It is a trivial piece of code to build with Gradle. The deployment of the library to MavenCentral is done 
by my another project [Jarbird](https://github.com/hkhc/jarbird). But it is undergoing major update, stay tuned.

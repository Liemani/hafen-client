# liefeni

Read this in other languages: [한국어](https://github.com/Liemani/liefeni/blob/main/README.ko.md)

## Address

- Discord: [https://discord.gg/hTWdyPGu99](https://discord.gg/hTWdyPGu99)

## How to run

- Download v0.1.0.zip from above discord server '배포' channel
- Download latest hafen.jar from discord also
- move latest hafen.jar to v0.1.0 directory
- Run hafen.jar

## How to use

You can run automation program by entering valid command in client built-in console
- `:<command> help`: print man page of `command` to System chat window
- command list
    - lmi
    - man
- example
```
:lmi AlignLog
```
> Run auomation program aligning log
- You can terminate automation program by pressing `ESC`

## Automation program list

- AlignLog: align log

## 목적

- Add automation program with adding least code to original code
    1. Modification to the original code should be limited to adding new lines whenever possible
- Original features can be removed for optimization
- There are no plans to add visual features
- There are no plans to add ability to interact with the gui

## Convention

- `_<identifier>`: Prepend `_` to identifiers with private access modifier
- Don't omit `invoker` of method which is not private method(ex> this.method(), ClassName.method())

## Next plan

- Add api to interact with items
- Create automation program building drying frame

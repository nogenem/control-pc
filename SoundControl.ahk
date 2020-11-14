#NoEnv  ; Recommended for performance and compatibility with future AutoHotkey releases.
; #notrayicon
#singleinstance force
; #Warn  ; Enable warnings to assist with detecting common errors.
SendMode Input  ; Recommended for new scripts due to its superior speed and reliability.
SetWorkingDir %A_ScriptDir%  ; Ensures a consistent starting directory.

; https://www.autohotkey.com/docs/KeyList.htm
^F7::Media_Play_Pause
^F8::Media_Prev
^F9::Media_Next
^F10::Volume_Mute
^F11::Volume_Up
^F12::Volume_Down
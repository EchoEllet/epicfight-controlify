# **Epic Fight: Controlify**

A simple mod that integrates [**Epic Fight**](https://modrinth.com/mod/epic-fight) 
with [**Controlify**](https://modrinth.com/mod/controlify) for full controller support.

<iframe width="560" height="315" src="https://www.youtube-nocookie.com/embed/PLZmGQZ5iAM" title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" allowfullscreen></iframe>

## **Controlify NeoForge Crash**

Currently, the latest version of **Controlify (v2.4.2)** crashes on **NeoForge 1.21.1**.  
A [patch](https://github.com/isXander/Controlify/pull/689) has already been submitted by an open-source contributor.

In the meantime, you can download the fixed file —  
`controlify-2.4.2+1.21.1-neoforge.jar` — from this [**unofficial release**](https://github.com/EchoEllet/Controlify/releases) on GitHub and use it until the official update is available.

## **Support**

This mod currently supports:

* Phantom Ascent skill (double-jump)
* Demolition Leap
* Epic Fight attacks without vanilla attacks in Epic Fight mode 
* Weapon innate skills and tooltip in inventory GUI
* Guard
* Preventing accidental item switching or offhand swapping during combat in Epic Fight mode
* Dodge and Step skills — also compatible with custom dodge skills from Epic Fight addons
* Knockdown wake-up skill
* Controlify-supported keybinds with descriptions and a radial menu for switching to vanilla mode or opening the skill
  editor screen

However, the **Skill Editor** and **Skill Tree** screens from Epic Fight are **not yet supported**.

## **Prototype**

While this mod allows most Epic Fight actions to be played smoothly with a controller,
it primarily serves as a quick **proof-of-concept**
for [this Epic Fight issue](https://github.com/Epic-Fight/epicfight/issues/2116),
as we are currently collaborating with the Epic Fight team.

We are also [refactoring the existing Epic Fight input system](https://github.com/Epic-Fight/epicfight/pull/2122) to
enable **native controller support** in future updates.  
This will make controller support a built-in feature of Epic Fight — eliminating the need for this mod — and provide a
cleaner, more stable experience with fewer hacks, workarounds, and bugs, ensuring that **all Epic Fight features** work
seamlessly with Controlify.

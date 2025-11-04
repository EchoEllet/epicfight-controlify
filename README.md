# 🎮 **Epic Fight: Controlify**

A mod that integrates [**Epic Fight**](https://modrinth.com/mod/epic-fight) with
[**Controlify: Forgified (Unofficial)**](https://www.curseforge.com/minecraft/mc-mods/controlify-forgified-unofficial)
for full controller support.

<iframe width="560" height="315" src="https://www.youtube-nocookie.com/embed/PLZmGQZ5iAM" title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" allowfullscreen></iframe>

---

## 🚀 Future Direction | NeoForge

As of **Epic Fight 21.13.3.2 (NeoForge 1.21.1)**, this mod’s functionality is **built-in** and fully supported.  
Installing it on NeoForge 1.21.1 may cause **undefined** or buggy behavior.

We **recommend** using the **latest Epic Fight NeoForge 1.21.1** with Controlify.  
If you’re still on legacy Forge 1.20.1, you can keep using it until Epic Fight addons migrate to NeoForge 1.21.1
(or newer versions).

This project is the result of collaboration with the Epic Fight team.  
However, the only officially supported integration is the built-in one included with
**Epic Fight NeoForge 1.21.1** and newer versions.

> Unlike the Epic Fight NeoForge 1.21.1 build, this backport does not support on-screen Controlify button guides,  
> since [Controlify: Forgified](https://www.curseforge.com/minecraft/mc-mods/controlify-forgified-unofficial)  
> is an unofficial backport of an older Controlify version.

## ⚔️ **Epic Fight Actions**

The Controlify integration supports:

- **Phantom Ascent** (double-jump)
- **Demolition Leap** (or any chargeable skill)
- Epic Fight attacks without vanilla attacks in Epic Fight mode
- Weapon innate skills and tooltips in the inventory GUI
- **Any Guard Skill**
- Preventing accidental item switching or offhand swapping during combat in Epic Fight mode
- **Dodge** and **Step** skills — also compatible with custom dodge skills from Epic Fight addons; as long as they don't
  override the vanilla Epic Fight behavior
- **Knockdown wake-up** skill
- Controlify-supported keybinds with descriptions and a radial menu for multiple actions:
    * Switching to vanilla mode
    * Opening the skill editor or skill tree
- **Skill Editor** and **Skill Tree** screens — with enhanced navigation support

## 📦 **Required Dependencies**

Make sure you have these mods installed:

- [**Epic Fight**](https://modrinth.com/mod/epic-fight)
- [**Controlify: Forgified (Unofficial)**](https://www.curseforge.com/minecraft/mc-mods/controlify-forgified-unofficial)
  - [YetAnotherConfigLib (YACL)](https://modrinth.com/mod/yacl) (required by Controlify)

## 🔧 **Compatibility**

### ✅ Fully Supported and Compatible Mods

- [Epic Fight](https://modrinth.com/mod/epic-fight)
- [Epic Fight: Skill Tree](https://modrinth.com/mod/epic-fight-skill-tree)

---

### ⚠️ Not Yet Compatible Mods

- [Epic Fight - Invincible Lib](https://www.curseforge.com/minecraft/mc-mods/epic-fight-invincible) — This mod registers
  custom keybinds and input handling, even for basic attacks, so it’s currently unsupported.  
  We have [submitted a GitHub pull request](https://github.com/GaylordFockerCN/EpicFight-Invincible/pull/2) to the
  author to address this issue.

- [Epic Fight - Sword Soaring](https://www.curseforge.com/minecraft/mc-mods/sword-soaring) — This mod introduces custom
  keybinds that require patching for full compatibility.  
  We have [submitted a GitHub pull request](https://github.com/GaylordFockerCN/SwordSoaring-Reborn/pull/7) to the
  author to address this issue.

---

### 🧩 Minor Incompatibilities

- [Weapons of Miracles](https://modrinth.com/mod/weapons-of-miracles) — This mod has custom jump input handling when
  using the *Natural Sprinter* skill, but aside from that, everything works as expected.

## 🐞 **Bug Reports**

Any issues when using this unofficial addon **should not be reported** to the Epic Fight or Controlify projects.  
Please [**submit them to this GitHub repository**](https://github.com/EchoEllet/epicfight-controlify/issues) instead.

---

## ⚠️ **Disclaimer**

> **This mod is NOT AN OFFICIAL MINECRAFT PRODUCT.  
> It is NOT APPROVED BY OR ASSOCIATED WITH MOJANG OR MICROSOFT.**
>
> **This mod is not affiliated with the Controlify project authors OR [**isXander**](https://github.com/isXander).**
> The logo/icon of this mod has been generated using AI.

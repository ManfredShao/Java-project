Klotski Puzzle
Project Overview
In this project, you will implement the classic Klotski Puzzle game using the Java programming
language. Players must move sliding blocks to guide a specific block to the target position.
1. Game Introduction
Klotski is a traditional Chinese puzzle game in which players need to move the "Cao Cao" block
out of the board's exit.
The game is typically played on a rectangular board with blocks of different sizes and shapes.
Players must carefully plan each move to complete the task successfully.
2. Project Requirements
For this project, you need to complete the following tasks:
Task 1: Game Initialization (10 points)
1. Game Start Interface: Implement a game start interface.
2. Board Initialization: For the basic requirements, the board should be initialized as a 4 x 5
grid with blocks of different sizes:
Cao Cao block: 2 x 2 size, 1 piece
Guan Yu block: 2 x 1 size, 1 piece
Other general blocks: 1 x 2 size, 4 pieces
Soldier blocks: 1 x 1 size, 4 pieces
3. Block Color Differentiation: Blocks of different sizes should be distinguished by different
colors.
4. Restart Function: Players can restart the game at any time, and the board should reset to
its initial state.
Task 2: Block Movement (20 points)
1. Block Movement: Players can control the movement of blocks (up, down, left, right). Blocks
can only move along empty spaces on the board and cannot overlap with other blocks.
2. Button Controls: The interface should include up, down, left, and right buttons for
controlling block movement.
3. Keyboard Controls: The game must also support keyboard controls for movement.
4. Boundary Detection: Blocks cannot move beyond the board's boundaries.
5. Collision Detection: Blocks cannot overlap with other blocks during movement.
6. Information Recording: At the beginning of a new game, record the number of moves and
other necessary information.
7. Movement Recording: Record each move and provide an undo function (completing this
function will earn advanced points).
Task 3: Victory Condition (5 points)
1. Victory Condition: The game is won when the "Cao Cao" block reaches the exit position, and
a victory prompt need to be displayed.
2. Victory Interface: The victory interface should display the number of moves taken by the
player.
Task 4: Multi-User Login (15 points)
1. Implement a login selection interface for both guests and registered users.
2. Guests can play without registration but do not have the functionality to save game
progress.
3. The user login interface includes a registration page and allows login after entering account
credentials.
4. After the program exits and is run again, previously registered users can still log in
Task 5: Save and Load Game (20 points)
1. Each user (except guests) can load previously saved games. The save file should be a single
file, and subsequent saves will overwrite the previous one (overwriting is the basic
requirement. Additional points would not be given if multiple save slots are implemented
per user).
2. From the game start interface, players can choose to load the last saved game. The file
should include the game board's status, the number of moves made so far, and other
necessary information.
3. Each user's save data is unique.
4. Manual saving is the basic requirement; implementing timed auto-save or auto-save on exit
will earn advanced points.
5. Save File Error Handling: If the save file is corrupted in format or content, the corrupted file
will not be loaded, and the game should continue running without crashing. (If your game is
capable of detecting save files that have been modified by others while still maintaining the
legitimacy of the save data，it will earn the advanced points.)
Task 6: Graphical User Interface (GUI) (10 points)
1. Implement a graphical interface for the game using JavaFX, Swing, or any other Java
graphical framework.
2. You can earn points for this task by completing the demo code provided in the course.
3. Independently creating a unique GUI will earn advanced points.
4. If your program relies on command-line input, you cannot earn full points for this task.
Task 7: Advanced Features (20 points)
Any features beyond the basic requirements can earn advanced points, including but not limited
to:
1. Interface Beautification: Enhanced graphics and visual effects.
2. Multi-Level Design: Design multiple levels of varying difficulty, with different board layouts
or block configurations for each level.
Required blocks: Cao Cao and Guan Yu blocks.
If multi-level functionality is added, provide corresponding buttons on the start
interface to access different levels.
3. Artificial Intelligence: Implement an AI algorithm to automatically solve the Klotski puzzle.
Different levels of intelligence will earn varying points.
4. Animation Effects: Add smooth animations for block movements.
5. Sound Effects and Background Music: Enhance the gaming experience with sound effects
and background music.
6. Time Attack Mode: Introduce a timed mode where players must complete the game within
a set time.
If a timed mode is added, the save and load operations must include the player's time
usage.
The victory interface should also display the time taken.
7. Props and Obstacles: Add props and obstacles to enrich gameplay.
If props and obstacles are added, the save and load operations must include this
information.
8. Online Spectating: Allow multiple users to log in simultaneously and spectate the game or
perform other operations online.
3. Notes
1. Code Standards: The code should be well-structured and commented for readability and
maintainability.
2. Compatibility: Ensure the code runs smoothly on different operating systems.
3. Testing: Thoroughly test all functionalities before submission to avoid major bugs. Program
crashes will result in point deductions.
Wishing everyone success in completing the project!
华容道项目概述

在本项目中，你将使用 Java 编程语言实现经典的华容道游戏。玩家需要移动滑块，将特定的“曹操”块引导到目标位置。
1. 游戏介绍

华容道是一款传统的中国益智游戏，玩家需要设法将“曹操”块移出棋盘出口。

游戏通常在一个矩形棋盘上进行，棋盘上有不同大小和形状的方块。玩家必须精心规划每一步操作，才能成功完成任务。
2. 项目要求

你需要完成以下任务：
任务 1：游戏初始化（10 分）

    游戏开始界面：实现游戏启动界面。

    棋盘初始化：实现一个 4x5 的棋盘，并初始化不同大小的方块：

        曹操方块：2x2 大小，1 个

        关羽方块：2x1 大小，1 个

        其他将领方块：1x2 大小，4 个

        士兵方块：1x1 大小，4 个

    方块颜色区分：不同大小的方块应使用不同颜色加以区分。

    重开功能：玩家可以随时重新开始游戏，棋盘应恢复初始状态。

任务 2：方块移动（20 分）

    方块移动：玩家可以控制方块向上、下、左、右移动。方块只能沿着空位移动，不能重叠。

    按钮控制：界面需提供上下左右的按钮以控制移动。

    键盘控制：游戏需支持键盘控制。

    边界检测：方块不能移动到棋盘边界之外。

    碰撞检测：方块移动时不能与其他方块重叠。

    信息记录：新游戏开始时需记录玩家步数等相关信息。

    移动记录：记录每一步操作，并提供“撤回操作”功能（实现该功能可获得额外加分）。

任务 3：胜利判断（5 分）

    胜利条件：当“曹操”方块到达出口位置时，游戏判定胜利，并显示胜利提示。

    胜利界面：胜利界面需展示玩家的移动步数。

任务 4：多用户登录（15 分）

    实现登录选择界面，支持游客和注册用户登录。

    游客可以直接游玩，但不支持保存游戏进度。

    用户登录界面应包含注册页面，并支持凭账号密码登录。

    程序退出后再次运行时，已注册用户仍能登录。

任务 5：游戏保存与读取（20 分）

    每个用户（游客除外）可以读取上次保存的游戏。保存文件应为单个文件，后续保存会覆盖原文件（实现多个存档不会额外加分）。

    在游戏开始界面，玩家可选择加载上次保存的游戏。保存文件需包括棋盘状态、步数及其他必要信息。

    每个用户的存档互相独立。

    实现手动保存即可获得基本分数，若实现定时自动保存或退出时自动保存，则可获得额外加分。

    保存文件错误处理：若保存文件格式或内容损坏，程序不应崩溃，应跳过加载并继续运行游戏。（若能识别是否为人为修改并判断合法性，也可获得额外加分）

任务 6：图形用户界面（GUI）（10 分）

    使用 JavaFX、Swing 或其他 Java 图形框架实现图形界面。

    若完成课程提供的演示代码即可获得本任务分数。

    独立设计并实现原创 GUI 可获得额外加分。

    若程序仅依赖命令行输入，将无法获得本任务满分。

任务 7：高级功能（20 分）

完成以下任意进阶功能均可获得额外加分，包括但不限于：

    界面美化：增强图形与视觉效果。

    多关卡设计：设计多个不同难度的关卡，每关有不同棋盘布局或方块配置（必须包含曹操与关羽）。

        启动界面需提供进入不同关卡的按钮。

    人工智能（AI）：实现 AI 算法自动解题，根据智能程度获得不同加分。

    动画效果：添加方块移动的动画。

    音效与背景音乐：添加背景音乐和音效以增强游戏体验。

    限时模式：设置倒计时限制，玩家需在限定时间内完成游戏。

        保存/读取需包括时间信息，胜利界面也应显示完成所用时间。

    道具与障碍物：引入道具与障碍物丰富玩法。

        保存/读取功能也需包含这些信息。

    在线观战：支持多用户同时登录，并可以观战或进行其他联机互动操作。

3. 备注

    代码规范：代码应结构清晰，并包含适当注释，便于阅读与维护。

    兼容性：确保代码可在不同操作系统上正常运行。

    测试：在提交前务必充分测试所有功能，避免重大 Bug。如程序崩溃将会扣分。

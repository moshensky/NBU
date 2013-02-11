<?php
    
    function wordToSlashes($word) 
    {       
        $newWord = "";
        for ($i = 1; $i < strlen($word); $i++) 
        {
            $newWord = $newWord."_ ";
        }      
        return $newWord."_";
    }

    // Querry random word from DB and return it
    function getWord() 
    {
        $id = db_query("SELECT MAX(id) as max_id FROM hangman_game") -> fetchField();
        $id = rand(1,$id);
        //echo "DEBUG $id";
        $result = db_query('SELECT * FROM hangman_game WHERE id = '.$id);
    
        foreach ($result as $record) 
        {
            $word = $record->word;
            $_SESSION['guessInfo'] = $record->info;

        }
        return $word;
    }

    function startNewGame()
    {
        // New game
        $theWord = getWord();
        $guessWord = wordToSlashes($theWord);
        $error_amount = 0;
        $_SESSION['theWord'] = $theWord;
        $_SESSION['guessWord'] = $guessWord;        
        $_SESSION['error_amount'] = $error_amount;
        $_SESSION['gameStatus'] = "playingGame";
        $_SESSION['message'] = "New game has started!";
    }

    function endGame($msg = "")
    {
        if ($msg == "win")
        {
            $_SESSION['message'] = "GAME OVER! <span style='color:green'><em>YOU WON!</em></span> Press new game or reload page.";
        }
        else if ($msg == "loose")
        {
            $_SESSION['message'] = "GAME OVER! <span style='color:red'><em>YOU LOST!</em></span> Press new game or reload page.";
        }
        
        $_SESSION['guessWord'] = "<span style='color:red'>".$_SESSION['theWord']."</span>";       
        $_SESSION['gameStatus'] = "startGame";        
        // start new game?
    }

    function playGameTurn()
    {
        // Getting the $letter value
        $letter = htmlentities(stripslashes($_POST['letter']));
        // Retrieving session variables
        $theWord = $_SESSION['theWord'];
        $guessWord = $_SESSION['guessWord'];
        $error_amount = $_SESSION['error_amount'];
        
        $foundLetter = false;
        for ($d = 0, $i = 0; $d < strlen($theWord); $d++, $i+=2) 
        {
            $charTheWord = substr($theWord, $d, 1);
            //$charGuessWord = substr($guessWord, $i, 1);
            if ($charTheWord == $letter)
            {
                $guessWord[$i] = $charTheWord;
                $foundLetter = true;
            }
        }       
       
        $_SESSION['guessWord'] = $guessWord;
        
        $temp_str = str_replace(" ", "", $guessWord);
        
        //echo "DEBUG $temp_str";
        
        if ($_SESSION['theWord'] == $temp_str) 
        {
            endGame("win");
        }
        if ($foundLetter == false) 
        {
            $error_amount++;
            $_SESSION['error_amount'] = $error_amount;
            // If the error_amount is higher as 9, the player lost
            if ($error_amount > 6) 
            {
                $_SESSION['error_amount'] = 7;
                endGame("loose");
            }
        }
    }

    // Resetting the message
    $_SESSION['message'] = "";
    
    if (!isset($_SESSION['gameStatus']) || (isset($_POST['submit']) && ($_POST['submit'] == "new game"))) 
    {
        // if user enters site for first time or pressed new game button - start new game
        startNewGame();
    }
    else if ($_SESSION['gameStatus'] == "endGame")
    {
        // if game has ended
        endGame("");
    } 
    else if ($_SESSION['gameStatus'] == "startGame")
    {
        // start new game
        startNewGame();
    }
    else if ($_SESSION['gameStatus'] == "playingGame")
    {        
        if (isset($_POST['letter'])) 
        {
            $temp_res = preg_match("/[A-Z\s_]/i", $_POST['letter']);
            if ( $temp_res > 0)
            {
                $_SESSION['message'] =  "game is playing";
                playGameTurn();
            }
            else if ($temp_res < 0)
            {
                // some error happend
                $_SESSION['message'] = "Only alphanumeric symbols are allowed!";
            } 
            else 
            {
                // entered symbol is not a letter
                $_SESSION['message'] = "Enter letter!";
            }
             
        }
        else 
        {
            $_SESSION['message'] = "Enter letter!";
        }         
    }

?>
    
    <section>        
        <article>
            <header>
                <h3>Try yourslef below :)</h3>
            </header>
            <div id="hangman">
                <p>
                    status: <?php echo $_SESSION['message']; ?>
                </p>   
                <img src="/sites/default/files/hangman/hangman<?php echo $_SESSION['error_amount'] ?>.png" alt="hangman game">
                <h4><?php echo $_SESSION['guessInfo'] .": ". $_SESSION['guessWord']; ?></h4>
                <form action="php-hangman-game-project-for-nbu-scripting-course" method="post">                                                      
                    <table>
                        <tr>
                            <td>
                                <label for="letter">Enter a letter:</label>
                            </td>
                            <td>
                                <input type="text" maxlength="1" size="1" id="letter" name="letter" />                               
                            </td>
                            <td>
                                <input type="submit" value="go" name="submit" />
                            </td>
                            <td>
                                <input type="submit" value="new game" name="submit" />
                            </td>
                        </tr>
                    </table>                       
                </form>                                 
            </div>  
        </article>
        <article>
            <header>
                <h3>My project for scripting languages course at NBU</h3>
            </header>
            <p>
                Game is written using php scripting language and MySql database for storing words and their descriptions.
                The code is pasted in standart drupal page through admin panel using php filter. User's session is used for storing needed variables.
                When user makes an error it's dynamicaly showen with a picture.
            </p>
        </article>
    </section>






<?php

namespace App\Cryptology;

use App\Cryptology\Zad1\PaddingOracleAttacker;

include 'autoload.php';

$attacker = new PaddingOracleAttacker();
$attacker->attack();

/*
 *  This file is part of ClansBanks-events.
 *
 *  Copyright 2021 ms5984 (Matt) <https://github.com/ms5984>
 *  Copyright 2020 Hempfest <https://github.com/Hempfest>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License or
 *  the license mentioned above.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.github.ms5984.clans.clansbanks.events;

import com.github.ms5984.clans.clansbanks.api.ClanBank;
import org.bukkit.event.Event;

/**
 * A base class for all bank related events.
 */
public abstract class BankEvent extends Event {

    protected final ClanBank clanBank;

    protected BankEvent(ClanBank clanBank, boolean async) {
        super(async);
        this.clanBank = clanBank;
    }

    /**
     * Get the ClanBank associated with this event
     * @return the ClanBank
     */
    public ClanBank getClanBank() {
        return clanBank;
    }
}

/*
 *  This file is part of the Haven & Hearth game client.
 *  Copyright (C) 2009 Fredrik Tolf <fredrik@dolda2000.com>, and
 *                     Björn Johannessen <johannessen.bjorn@gmail.com>
 *
 *  Redistribution and/or modification of this file is subject to the
 *  terms of the GNU Lesser General Public License, version 3, as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  Other parts of this source tree adhere to other copying
 *  rights. Please see the file `COPYING' in the root directory of the
 *  source tree for details.
 *
 *  A copy the GNU Lesser General Public License is distributed along
 *  with the source tree of which this file is a part in the file
 *  `doc/LPGL-3'. If it is missing for any reason, please see the Free
 *  Software Foundation's website at <http://www.fsf.org/>, or write
 *  to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *  Boston, MA 02111-1307 USA
 */

package haven.glsl;

import java.util.*;

public class Block extends Statement {
    public final List<Statement> stmts = new LinkedList<Statement>();

    public Block(Statement... stmts) {
	for(Statement s : stmts)
	    this.stmts.add(s);
    }

    public final static class Local extends Variable {
	private Local(Type type, Symbol name) {
	    super(type, name);
	}

	private class Def extends Statement {
	    private final Expression init;

	    private Def(Expression init) {
		this.init = init;
	    }

	    public Def process(Context ctx) {
		return(new Def(init.process(ctx)));
	    }

	    public void output(Output out) {
		out.write(type.name(out.ctx));
		out.write(" ");
		out.write(name);
		if(init != null) {
		    out.write(" = ");
		    init.output(out);
		}
		out.write(";");
	    }
	}
    }

    public Local local(Type type, Symbol name, Expression init) {
	Local ret = new Local(type, name);
	add(ret.new Def(init));
	return(ret);
    }

    public Local local(Type type, String prefix, Expression init) {
	return(local(type, new Symbol.Gen(prefix), init));
    }

    public Local local(Type type, Expression init) {
	return(local(type, new Symbol.Gen(), init));
    }

    public void add(Statement stmt) {
	stmts.add(stmt);
    }

    public void add(Expression expr) {
	add(Statement.expr(expr));
    }

    public Block process(Context ctx) {
	Block ret = new Block();
	for(Statement s : stmts)
	    ret.add(s.process(ctx));
	return(ret);
    }

    public void trail(Output out) {
	out.write("{\n");
	out.indent++;
	for(Statement s : stmts) {
	    out.indent();
	    s.output(out);
	    out.write("\n");
	}
	out.indent--;
	out.write("}\n");
    }

    public void output(Output out) {
	out.indent();
	trail(out);
    }
}
